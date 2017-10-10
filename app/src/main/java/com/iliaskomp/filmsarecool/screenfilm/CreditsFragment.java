package com.iliaskomp.filmsarecool.screenfilm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iliaskomp.filmsarecool.R;
import com.iliaskomp.filmsarecool.models.credits.Actor;
import com.iliaskomp.filmsarecool.models.credits.Credits;
import com.iliaskomp.filmsarecool.models.credits.CrewMember;
import com.iliaskomp.filmsarecool.network.ActorListImageFetching;
import com.iliaskomp.filmsarecool.network.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_BASE_URL;
import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_KEY;

/**
 * Created by IliasKomp on 09/10/17.
 */

public class CreditsFragment extends Fragment{
    private static final String ARG_FILM_ID  = "film_id";

    private RequestQueue mRequestQueue;

    private RecyclerView mActorRecyclerView;
    private ActorAdapter mActorAdapter;

    private TextView mDirectorText;
    private TextView mWriterText;
    private TextView mComposerText;

//    private List<Actor> mActors;
    private Credits mCredits;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRequestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();
            String filmId = (String)getArguments().get(ARG_FILM_ID);
            fetchCredits(filmId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_list, container, false);

        mActorRecyclerView = (RecyclerView) view.findViewById(R.id.actor_list_recycler_view);
        mActorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDirectorText = (TextView) view.findViewById(R.id.director_text_view);
        mWriterText = (TextView) view.findViewById(R.id.writer_text_view);
        mComposerText = (TextView) view.findViewById(R.id.composer_text_view);

        return view;
    }

    private void fetchCredits(String filmId) {
        String requestUrl = API_BASE_URL + "movie/" + filmId + "/credits" + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mCredits = deserializeResult(response);
                updateUI(mCredits);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);
    }

    private void updateUI(Credits credits) {
        mActorAdapter = new ActorAdapter(credits.getActors());
        mActorRecyclerView.setAdapter(mActorAdapter);

        mDirectorText.setText(credits.getDirectorsString());
        mWriterText.setText(credits.getWritersString());
        mComposerText.setText(credits.getComposersString());
    }

    private Credits deserializeResult(JSONObject response) {
        Credits credits = new Credits();
        List<Actor> actors = new ArrayList<>();

        try {
            JSONArray cast = response.getJSONArray("cast");
            JSONArray crew = response.getJSONArray("crew");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < cast.length(); i++) {
                Actor actor = gson.fromJson(String.valueOf(cast.getJSONObject(i)), Actor.class);
                actors.add(actor);
            }

            for (int i = 0; i < crew.length(); i++) {
                JSONObject crewObject = crew.getJSONObject(i);
                String job = (String) crewObject.get("job");

                switch (job) {
                    case CrewMember.DIRECTOR:
                        CrewMember director = CrewMember.constructCrewMember(crewObject);
                        credits.addDirector(director);
                        break;
                    case CrewMember.WRITER:
                        CrewMember writer = CrewMember.constructCrewMember(crewObject);
                        credits.addWriter(writer);
                        break;
                    case CrewMember.COMPOSER:
                        CrewMember composer = CrewMember.constructCrewMember(crewObject);
                        credits.addComposer(composer);
                        break;
                }
            }

        } catch (JSONException e) {
//            Toast.makeText(getActivity(), "There has been a GSON error.", Toast.LENGTH_LONG).show();
        }

        credits.setActors(actors);
        return credits;
    }

    private class ActorHolder extends RecyclerView.ViewHolder {
        private ImageView mActorImageView;
        private TextView mActorName;
        private TextView mActorCharacter;

        public ActorHolder(View itemView) {
            super(itemView);

            mActorImageView = (ImageView) itemView.findViewById(R.id.actor_item_image);
            mActorName = (TextView) itemView.findViewById(R.id.actor_item_name);
            mActorCharacter = (TextView) itemView.findViewById(R.id.actor_item_character);
        }

        public void bindActor(Actor actor) {
            mActorName.setText(actor.getName());
            mActorCharacter.setText(actor.getCharacter());
            ActorListImageFetching.setActorImage(actor, mActorImageView, getActivity());

//            mDirectorText.setText(actor.getDirector());
//            mWriterText.setText(actor.getWriter());
//            mComposerText.setText(actor.getComposer());

        }
    }

    private class ActorAdapter extends RecyclerView.Adapter<ActorHolder> {
        private List<Actor> mActors;

        public ActorAdapter(List<Actor> actors) {
            mActors = actors;
        }

        @Override
        public ActorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_actor, parent, false);
            return new ActorHolder(view);
        }

        @Override
        public void onBindViewHolder(ActorHolder holder, int position) {
            Actor actor = mActors.get(position);
            holder.bindActor(actor);
        }

        @Override
        public int getItemCount() {
            return mActors.size();
        }
    }

    public static CreditsFragment newInstance(String filmId) {
        Bundle args = new Bundle();
        args.putString(ARG_FILM_ID, filmId);

        CreditsFragment fragment = new CreditsFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

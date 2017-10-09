package com.iliaskomp.filmsarecool.filmview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iliaskomp.filmsarecool.R;
import com.iliaskomp.filmsarecool.filmmodel.Actor;
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

public class ActorListFragment extends Fragment{
    private static final String ARG_FILM_ID  = "film_id";

    private RequestQueue mRequestQueue;

    private RecyclerView mFilmRecyclerView;
    private ActorAdapter mActorAdapter;

    private List<Actor> mActors;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRequestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();
            String filmId = (String)getArguments().get(ARG_FILM_ID);
            fetchActorList(filmId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_list, container, false);

        mFilmRecyclerView = (RecyclerView) view.findViewById(R.id.actor_list_recycler_view);
        mFilmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    private void fetchActorList(String filmId) {
        String requestUrl = API_BASE_URL + "movie/" + filmId + "/credits" + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mActors = deserializeResult(response);
//                updateUI(mActors);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);
    }

    private List<Actor> deserializeResult(JSONObject response) {
        List<Actor> actors = new ArrayList<>();

        try {
            JSONArray results = response.getJSONArray("cast");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < results.length(); i++) {
                Actor actor = gson.fromJson(String.valueOf(results.getJSONObject(i)), Actor.class);
                actors.add(actor);
            }
        } catch (JSONException e) {
//            Toast.makeText(getActivity(), "There has been a GSON error.", Toast.LENGTH_LONG).show();
        }
        return actors;
    }

    private class ActorHolder extends RecyclerView.ViewHolder {

        public ActorHolder(View itemView) {
            super(itemView);
        }

        public void bindActor(Actor actor) {

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
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
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

    public static ActorListFragment newInstance(String filmId) {
        Bundle args = new Bundle();
        args.putString(ARG_FILM_ID, filmId);

        ActorListFragment fragment = new ActorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

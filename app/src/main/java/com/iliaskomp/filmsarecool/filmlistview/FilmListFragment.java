package com.iliaskomp.filmsarecool.filmlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iliaskomp.filmsarecool.filmview.FilmActivity;
import com.iliaskomp.filmsarecool.R;
import com.iliaskomp.filmsarecool.filmmodel.FilmShortInfo;
import com.iliaskomp.filmsarecool.network.FilmPosterFetching;
import com.iliaskomp.filmsarecool.network.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_BASE_URL;
import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_KEY;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmListFragment extends Fragment {
    private static final String TAG = "FilmListFragment";

    private static final String ARG_QUERY_STRING = "query_string";

    private RequestQueue mRequestQueue;

    private RecyclerView mFilmRecyclerView;
    private FilmAdapter mFilmAdapter;

    private List<FilmShortInfo> mFilms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();

        if (getArguments() != null) {
            String query = getArguments().getString(ARG_QUERY_STRING);
            fetchFilmList(query);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

        mFilmRecyclerView = (RecyclerView) view.findViewById(R.id.film_list_recycler_view);
        mFilmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    public void updateUI(List<FilmShortInfo> films) {
        mFilmAdapter = new FilmAdapter(films);
        mFilmRecyclerView.setAdapter(mFilmAdapter);
    }

    void fetchFilmList(String query) {
        String requestUrl = API_BASE_URL + "search/movie?" + "query=" + query + "&api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mFilms = deserializeResult(response);
                updateUI(mFilms);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);
    }



    private List<FilmShortInfo> deserializeResult(JSONObject response) {
        List<FilmShortInfo> filmsShort = new ArrayList<>();

        try {
            //int numberOfPages = (int) response.get("total_pages");
            int numberOfResults = (int) response.get("total_results");
            JSONArray results = response.getJSONArray("results");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < numberOfResults; i++) {
                FilmShortInfo film = gson.fromJson(String.valueOf(results.getJSONObject(i)), FilmShortInfo.class);
                filmsShort.add(film);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "There has been a GSON error.", Toast.LENGTH_LONG).show();
        }
        return filmsShort;
    }

    private class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitleTextView;
        public ImageView mPosterImageView;
        public FilmShortInfo mFilm;

        public FilmHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.film_item_title_text_view);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.film_item_poster_image);
        }

        public void bindFilm(FilmShortInfo film) {
            mFilm = film;
            mTitleTextView.setText(film.getTitle());
            FilmPosterFetching.setPosterImage(film, mPosterImageView, getActivity());
        }

        @Override
        public void onClick(View view) {
            Intent intent = FilmActivity.newIntent(getActivity(), String.valueOf(mFilm.getId()));
            startActivity(intent);
        }
    }



    private class FilmAdapter extends  RecyclerView.Adapter<FilmHolder> {
        private List<FilmShortInfo> mFilms;

        public FilmAdapter(List<FilmShortInfo> films) {
            mFilms = films;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_film, parent, false);
            return new FilmHolder(view);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            FilmShortInfo film = mFilms.get(position);
            holder.bindFilm(film);
        }

        @Override
        public int getItemCount() {
            return mFilms.size();
        }
    }

    public static FilmListFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(ARG_QUERY_STRING, query);

        FilmListFragment fragment = new FilmListFragment();
        fragment.setArguments(args);
        return fragment;
    }

}

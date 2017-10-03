package com.iliaskomp.filmsarecool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iliaskomp.filmsarecool.ConfigApi.API_BASE_URL;
import static com.iliaskomp.filmsarecool.ConfigApi.API_KEY;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmListFragment extends Fragment {
    private static final String TAG = "FilmListFragment";

    private RecyclerView mFilmRecyclerView;
    private FilmAdapter mFilmAdapter;

    private EditText mSearchFilmsEditText;

    private List<MovieShortInfo> mFilms;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_list, container, false);

        mFilmRecyclerView = (RecyclerView) view.findViewById(R.id.film_list_recycler_view);
        mFilmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchFilmsEditText = (EditText) view.findViewById(R.id.search_films_edit_text);

        Button searchButton = (Button) view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = String.valueOf(mSearchFilmsEditText.getText());
                fetchMovieList(searchQuery);
            }
        });

//        updateUI();
        return view;
    }

    public void updateUI(List<MovieShortInfo> films) {
        mFilmAdapter = new FilmAdapter(films);
        mFilmRecyclerView.setAdapter(mFilmAdapter);
    }

    void fetchMovieList(String query) {
        Log.d(TAG, "fetchMovieList called");

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
                Toast.makeText(getActivity(), "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(request);
    }

    private List<MovieShortInfo> deserializeResult(JSONObject response) {
        Log.d(TAG, "deserializeResult called");

        List<MovieShortInfo> filmsShort = new ArrayList<>();

        try {
//            int numberOfPages = (int) response.get("total_pages");
            int numberOfResults = (int) response.get("total_results");
            JSONArray results = response.getJSONArray("results");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < numberOfResults; i++) {
                MovieShortInfo film = gson.fromJson(String.valueOf(results.getJSONObject(i)), MovieShortInfo.class);
                filmsShort.add(film);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "There has been a JSON error with the application.", Toast.LENGTH_LONG).show();
        }
        return filmsShort;
    }

    private class FilmHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        public FilmHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }

    private class FilmAdapter extends  RecyclerView.Adapter<FilmHolder> {
        private List<MovieShortInfo> mFilms;

        public FilmAdapter(List<MovieShortInfo> films) {
            mFilms = films;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new FilmHolder(view);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            MovieShortInfo film = mFilms.get(position);
            holder.mTitleTextView.setText(film.getTitle());
        }

        @Override
        public int getItemCount() {
            return mFilms.size();
        }
    }

}

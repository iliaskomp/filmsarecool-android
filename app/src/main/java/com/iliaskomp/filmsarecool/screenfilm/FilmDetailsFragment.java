package com.iliaskomp.filmsarecool.screenfilm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.iliaskomp.filmsarecool.models.film.FilmFullInfo;
import com.iliaskomp.filmsarecool.network.FilmPosterFetching;
import com.iliaskomp.filmsarecool.network.RequestQueueSingleton;

import org.json.JSONObject;

import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_BASE_URL;
import static com.iliaskomp.filmsarecool.config.TmdbConfig.API_KEY;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmDetailsFragment extends Fragment {
    public static final String ARG_FILM_ID = "film_id";

    private static RequestQueue mRequestQueue;

    private TextView mTitleText;
    private TextView mRuntimeText;
    private TextView mGenresText;
    private TextView mBudgetText;
    private TextView mRevenueText;
    private ImageView mPosterImage;
    private TextView mImdbRatingText;
    private TextView mTmdbRatingText;
    private TextView mOverviewText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRequestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();

            String filmId = (String)getArguments().get(ARG_FILM_ID);
            fetchFilmInfo(filmId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_details, container, false);

        mTitleText = (TextView) view.findViewById(R.id.title_text_view);
        mRuntimeText = (TextView) view.findViewById(R.id.runtime_text_view);
        mGenresText = (TextView) view.findViewById(R.id.genres_text_view);
        mBudgetText = (TextView) view.findViewById(R.id.budget_text_view);
        mRevenueText = (TextView) view.findViewById(R.id.revenue_text_view);
        mPosterImage = (ImageView) view.findViewById(R.id.poster_image_view);
        mImdbRatingText = (TextView) view.findViewById(R.id.imdb_rating_text_view);
        mTmdbRatingText = (TextView) view.findViewById(R.id.tmdb_rating_text_view);
        mOverviewText = (TextView) view.findViewById(R.id.overview_text_view);

        return view;
    }

    private void updateUI(FilmFullInfo film) {
        mTitleText.setText(film.getTitle());
        mRuntimeText.setText(film.getRuntimeString());
        mGenresText.setText(film.getGenresString());
        mOverviewText.setText(film.getOverview());
        mBudgetText.setText(film.getBudget());
        mRevenueText.setText(film.getRevenue());
        mTmdbRatingText.setText(film.getVoteAverage());

        FilmPosterFetching.setPosterImage(film, mPosterImage, getActivity());
    }

    void fetchFilmInfo(String filmId) {

        String requestUrl = API_BASE_URL + "movie/" + filmId + "?api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                FilmFullInfo film = deserializeResult(response);
                updateUI(film);
                // getCredits, getVideos
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);

    }



    private FilmFullInfo deserializeResult(JSONObject response) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        FilmFullInfo film = gson.fromJson(String.valueOf(response), FilmFullInfo.class);
        return film;
    }

    public static FilmDetailsFragment newInstance(String filmId) {
        Bundle args = new Bundle();
        args.putString(ARG_FILM_ID, filmId);

        FilmDetailsFragment fragment = new FilmDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

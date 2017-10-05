package com.iliaskomp.filmsarecool;

import android.graphics.Bitmap;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.iliaskomp.filmsarecool.TmdbConfig.API_BASE_URL;
import static com.iliaskomp.filmsarecool.TmdbConfig.API_IMAGE_BASE_URL;
import static com.iliaskomp.filmsarecool.TmdbConfig.API_KEY;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmListFragment extends Fragment {
    private static final String TAG = "FilmListFragment";

    private static final String ARG_QUERY_STRING = "query_string";

    private RequestQueue mRequestQueue;

    private RecyclerView mFilmRecyclerView;
    private FilmAdapter mFilmAdapter;

    private List<MovieShortInfo> mFilms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRequestQueue = RequestQueueSingleton.getInstance(getActivity()).getRequestQueue();

        if (getArguments() != null) {
            String query = getArguments().getString(ARG_QUERY_STRING);
            fetchMovieList(query);
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

    public void updateUI(List<MovieShortInfo> films) {
        mFilmAdapter = new FilmAdapter(films);
        mFilmRecyclerView.setAdapter(mFilmAdapter);
    }

    void fetchMovieList(String query) {
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



    private List<MovieShortInfo> deserializeResult(JSONObject response) {
        List<MovieShortInfo> filmsShort = new ArrayList<>();

        try {
            //int numberOfPages = (int) response.get("total_pages");
            int numberOfResults = (int) response.get("total_results");
            JSONArray results = response.getJSONArray("results");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < numberOfResults; i++) {
                MovieShortInfo film = gson.fromJson(String.valueOf(results.getJSONObject(i)), MovieShortInfo.class);
                filmsShort.add(film);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "There has been a GSON error.", Toast.LENGTH_LONG).show();
        }
        return filmsShort;
    }

    private class FilmHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public ImageView mPosterImageView;

        public FilmHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.film_item_title_text_view);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.film_item_poster_image);
        }

        public void bindFilm(MovieShortInfo film) {
            mTitleTextView.setText(film.getTitle());
            setPosterImage(film);
        }

        private void setPosterImage(final MovieShortInfo film) {
            String posterPath = film.getPosterPath();
            String imageRequestUrl = API_IMAGE_BASE_URL + TmdbConfig.PosterSize.selected + posterPath;

            if (posterPath == null) {
                setDefaultPoster(mPosterImageView);
            } else {
                ImageRequest imageRequest = new ImageRequest(
                        imageRequestUrl,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                film.setPosterImage(response);
                                mPosterImageView.setImageBitmap(response);
                            }
                        },
                        0,
                        0,
                        ImageView.ScaleType.CENTER_CROP,
                        Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                setDefaultPoster(mPosterImageView);
                            }
                        });
                mRequestQueue.add(imageRequest);
            }
        }
    }

    public void setDefaultPoster(ImageView mPosterImageView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mPosterImageView.setImageDrawable(getActivity().getDrawable(R.drawable.noposter_w92));
        } else {
            mPosterImageView.setImageDrawable(getResources().getDrawable(R.drawable.noposter_w92));
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
            View view = layoutInflater.inflate(R.layout.list_item_film, parent, false);
            return new FilmHolder(view);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            MovieShortInfo film = mFilms.get(position);
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

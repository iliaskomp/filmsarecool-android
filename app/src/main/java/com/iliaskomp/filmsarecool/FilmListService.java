package com.iliaskomp.filmsarecool;

import android.content.Context;
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

import static com.iliaskomp.filmsarecool.ConfigApi.*;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmListService {
    private Context mContext;
    private RequestQueue mRequestQueue;
    private List<FilmShortInfo> films;

    FilmListService(Context context, RequestQueue requestQueue) {
        mContext = context;
        mRequestQueue = requestQueue;
    }

    //TODO if multiple pages
    List<FilmShortInfo> getMovieList(String query) {
        String requestUrl = API_BASE_URL + "search/movie?" + "query=" + query + "&api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                films = deserializeResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "There has been an error with your request.", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);

        return films;
    }

    private List<FilmShortInfo> deserializeResult(JSONObject response) {
        List<FilmShortInfo> filmsShort = new ArrayList<>();

        try {
//            int numberOfPages = (int) response.get("total_pages");
            int numberOfResults = (int) response.get("total_results");
            JSONArray results = response.getJSONArray("results");

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            for (int i = 0; i < numberOfResults; i++) {
                FilmShortInfo film = gson.fromJson(String.valueOf(results.getJSONObject(i)), FilmShortInfo.class);
                filmsShort.add(film);
            }
        } catch (JSONException e) {
            Toast.makeText(mContext, "There has been a JSON error with the application.", Toast.LENGTH_LONG).show();
        }
        return filmsShort;
    }
}

package com.iliaskomp.filmsarecool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private final static String API_KEY = "1d9b5b41baa47611c75d9537cd59c830";
    private final static String API_BASE_URL = "https://api.themoviedb.org/3/";

    private RequestQueue mRequestQueue;

    private TextView mMoviesListText;
    private EditText mSearchFilmsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);

        Button searchButton = (Button) findViewById(R.id.search_button);
        mMoviesListText = (TextView) findViewById(R.id.movies_list_text);
        mSearchFilmsEditText = (EditText) findViewById(R.id.search_films_edit_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoviesListText.setText("Hello World!!!");
                getMovieList(mSearchFilmsEditText.getText().toString());
            }
        });
    }

//    TODO if multiple pages
    private void getMovieList(String query) {
        String requestUrl = API_BASE_URL + "search/movie?" + "query=" + query + "&api_key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deserializeResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "There has been an error with your request", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(request);
    }

    private void deserializeResult(JSONObject response) {
        System.out.println("On Response:\n" + response);
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        try {
            int numberOfPages = (int) response.get("total_pages");
            int numberOfResults = (int) response.get("total_results");
            System.out.println("numberOfPages: " + numberOfPages);

            JSONObject filmObject = response.getJSONArray("results").getJSONObject(0);
            FilmShortInfo film = gson.fromJson(String.valueOf(filmObject), FilmShortInfo.class);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

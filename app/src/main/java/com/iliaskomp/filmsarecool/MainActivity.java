package com.iliaskomp.filmsarecool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FilmListService mFilmListService;

    private TextView mMoviesListText;
    private EditText mSearchFilmsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        mFilmListService = new FilmListService(this, requestQueue);

        Button searchButton = (Button) findViewById(R.id.search_button);
        mMoviesListText = (TextView) findViewById(R.id.movies_list_text);
        mSearchFilmsEditText = (EditText) findViewById(R.id.search_films_edit_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoviesListText.setText("Hello World!!!");
                List<FilmShortInfo> films  = mFilmListService.getMovieList(mSearchFilmsEditText.getText().toString());
                System.out.println("films: " + films);
            }
        });
    }




}

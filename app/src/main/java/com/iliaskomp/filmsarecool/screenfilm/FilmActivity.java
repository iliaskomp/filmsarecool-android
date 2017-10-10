package com.iliaskomp.filmsarecool.screenfilm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.iliaskomp.filmsarecool.R;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmActivity extends AppCompatActivity {

    private static final String EXTRA_FILM_ID = "com.iliaskomp.filmsarecool.film_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_fragments);

        String filmId = getIntent().getStringExtra(EXTRA_FILM_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {

            Fragment filmFragment = FilmDetailsFragment.newInstance(filmId);
            Fragment actorsFragment = CreditsFragment.newInstance(filmId);

            fm.beginTransaction()
                    .add(R.id.fragment_container_1, filmFragment)
                    .add(R.id.fragment_container_2, actorsFragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context context, String filmId) {
        Intent intent = new Intent(context, FilmActivity.class);
        intent.putExtra(EXTRA_FILM_ID, filmId);
        return intent;
    }
}

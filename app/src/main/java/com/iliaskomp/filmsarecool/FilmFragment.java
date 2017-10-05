package com.iliaskomp.filmsarecool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmFragment extends Fragment {
    public static final String ARG_FILM_ID = "film_id";

    private String mFilmId;
    private TextView mFilmTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilmId = (String)getArguments().get(ARG_FILM_ID);

        if (getArguments() != null) {
            mFilmId = (String)getArguments().get(ARG_FILM_ID);
//            fetchFilmInfo()
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film, container, false);

        mFilmTitle = (TextView) view.findViewById(R.id.film_title_text_view);
        mFilmTitle.setText("Helllo World");


        return view;

    }

    public static FilmFragment newInstance(String filmId) {
        Bundle args = new Bundle();
        args.putString(ARG_FILM_ID, filmId);

        FilmFragment fragment = new FilmFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

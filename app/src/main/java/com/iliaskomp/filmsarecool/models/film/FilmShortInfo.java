package com.iliaskomp.filmsarecool.models.film;

import android.graphics.Bitmap;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmShortInfo {
    private int id;
    private boolean video;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private String backdropPath;
    private String adult;
    private Bitmap posterImage;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterImage(Bitmap posterImage) {
        this.posterImage = posterImage;
    }
}

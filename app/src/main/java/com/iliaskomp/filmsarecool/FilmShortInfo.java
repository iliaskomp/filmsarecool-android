package com.iliaskomp.filmsarecool;

import android.graphics.Bitmap;

/**
 * Created by IliasKomp on 12/09/17.
 */

public class FilmShortInfo {
    private int voteCount;
    private int id;
    private boolean video;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private int[] genreIds;
    private String backdropPath;
    private String adult;
    private String overview;
    private String releaseDate;
    private Bitmap posterImage;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Bitmap getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(Bitmap posterImage) {
        this.posterImage = posterImage;
    }
}

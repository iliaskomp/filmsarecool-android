package com.iliaskomp.filmsarecool;

import java.util.ArrayList;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmFullInfo extends FilmShortInfo {
    private int voteCount;
    private int budget;
    private String homepage;
    private String imdbId;
    private int revenue;
    private int runtime;
    private String status;
    private String tagline;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private ArrayList<Genre> genres;

    public String getRuntimeString() {
        return runtime + " minutes";
    }

    public String getGenresString() {
        switch (genres.size()) {
            case 0:
                return "";
            case 1:
                return genres.get(0).getName();
            case 2:
                return genres.get(0).getName() + ", " + genres.get(1).getName();
            default:
                return genres.get(0).getName() + ", " + genres.get(1).getName() + ", " + genres.get(2).getName();
        }
    }

    public String getOverview() {
        return overview;
    }

    public String getDirector() {
        return "Nolan Test";
    }

    public String getWriter() {
        return "Nolan Test";
    }

    public String getComposer() {
        return "Nolan Test";
    }

    public String getBudget() {
        return "$" + budget;
    }

    public String getRevenue() {
        return "$" + revenue;
    }

    public String getVoteAverage() {
        return String.valueOf(voteAverage);
    }

    private class Genre {
        int id;
        String name;

        public String getName() {
            return name;
        }
    }
}

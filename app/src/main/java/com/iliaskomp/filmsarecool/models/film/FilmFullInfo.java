package com.iliaskomp.filmsarecool.models.film;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by IliasKomp on 05/10/17.
 */

public class FilmFullInfo extends FilmShortInfo {

    private class Genre {
        int id;
        String name;

        public String getName() {
            return name;
        }
    }

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

    private String getRuntimeString() {
        return runtime == 0 ? "" : runtime + " minutes";
    }

    private String getGenresString() {
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

    public String getRuntimeGenresString() {
        String runtime = getRuntimeString();
        String genres = getGenresString();

        if (!runtime.isEmpty() && !genres.isEmpty()) {
            return getRuntimeString() + " - " + getGenresString();
        } else if (runtime.isEmpty() && genres.isEmpty()) {
            return "";
        } else if (runtime.isEmpty()) {
            return getGenresString();
        } else if (genres.isEmpty()) {
            return getRuntimeString();
        }
        return "";
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

    private String getBudget() {
        return String.format(Locale.US, "%,d", budget);
    }

    private String getRevenue() {
        return String.format(Locale.US, "%,d", revenue);
    }

    public String getBudgetRevenueString() {
        String budget = getBudget();
        String revenue = getRevenue();

        if (!isNullorZero(budget) && !isNullorZero(revenue)) {
            return "$" + getBudget() + " - " + "$" + getRevenue();
        } else if (isNullorZero(budget) && isNullorZero(revenue)) {
            return "";
        } else if (isNullorZero(budget)) {
            return "$" + getRevenue();
        } else if (isNullorZero(revenue)) {
            return "$" + getBudget();
        }
        return "";
    }

    public String getReleaseYear() {
        if (releaseDate != null) {
            return releaseDate.substring(0,4);
        }
        return "";
    }

    public String getVoteAverage() {
        return String.valueOf(voteAverage);
    }

    private boolean isNullorZero(String s) {
        return s.isEmpty() || s.equals("0");
    }
}

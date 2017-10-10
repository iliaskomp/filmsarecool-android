package com.iliaskomp.filmsarecool.models.credits;

/**
 * Created by IliasKomp on 09/10/17.
 */

public class Actor {
    private int id;
    private String name;
    private String character;
    private String profilePath;

    private String creditId;
    private int castId;
    private int order;


    public String getName() {
        return name;
    }

    public String getCharacter() {
        return character;
    }

    public String getProfilePath() {
        return profilePath;
    }
}

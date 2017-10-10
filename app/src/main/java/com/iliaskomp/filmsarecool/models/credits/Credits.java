package com.iliaskomp.filmsarecool.models.credits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IliasKomp on 10/10/17.
 */

public class Credits {
    private List<Actor> mActors;
    private List<CrewMember> directors = new ArrayList<>();
    private List<CrewMember> writers = new ArrayList<>();
    private List<CrewMember> composers = new ArrayList<>();

    //    private List<CrewMember> crew;


    public List<Actor> getActors() {
        return mActors;
    }

    public void setActors(List<Actor> actors) {
        this.mActors = actors;
    }

    public String getDirectorsString() {
        return getStringFromList(directors);
    }

    public void addDirector(CrewMember director) {
        directors.add(director);
    }

    public String getWritersString() {
        return getStringFromList(writers);
    }

    public void addWriter(CrewMember writer) {
        writers.add(writer);
    }

    public String getComposersString() {
        return getStringFromList(composers);
    }

    public void addComposer(CrewMember composer) {
        composers.add(composer);
    }

    private String getStringFromList(List<CrewMember> members) {
        String string = "";
        if (!members.isEmpty()) {
            string += members.get(0).getName();
            for (int i = 1; i < members.size(); i++) {
                string += ", " + members.get(i).getName();
            }
        } else {
            string += " - ";
        }

        return string;
    }
}

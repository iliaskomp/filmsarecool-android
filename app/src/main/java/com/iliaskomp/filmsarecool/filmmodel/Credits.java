package com.iliaskomp.filmsarecool.filmmodel;

import java.util.List;

/**
 * Created by IliasKomp on 10/10/17.
 */

public class Credits {
    private List<Actor> mActors;
    private CrewMember director;
    private CrewMember writer;
    private CrewMember composer;

    //    private List<CrewMember> crew;


    public List<Actor> getActors() {
        return mActors;
    }

    public void setActors(List<Actor> actors) {
        this.mActors = actors;
    }

    public CrewMember getDirector() {
        return director;
    }

    public void setDirector(CrewMember director) {
        this.director = director;
    }

    public CrewMember getWriter() {
        return writer;
    }

    public void setWriter(CrewMember writer) {
        this.writer = writer;
    }

    public CrewMember getComposer() {
        return composer;
    }

    public void setComposer(CrewMember composer) {
        this.composer = composer;
    }


}

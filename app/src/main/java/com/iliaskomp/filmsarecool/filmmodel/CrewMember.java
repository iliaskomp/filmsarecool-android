package com.iliaskomp.filmsarecool.filmmodel;

/**
 * Created by IliasKomp on 10/10/17.
 */

public class CrewMember {
    private int id;
    private String job;
    private String name;
    private String profilePath;


    public CrewMember(int id, String job, String name, String profilePath) {
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    private void setId(int id) {
        this.id = id;
    }

    private void setJob(String job) {
        this.job = job;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}

package com.iliaskomp.filmsarecool.models.credits;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IliasKomp on 10/10/17.
 */

public class CrewMember {
    // Constants for job value from tmdb api
    public static final String DIRECTOR = "Director";
    public static final String WRITER = "Screenplay";
    public static final String COMPOSER = "Original Music Composer";

    private int id;
    private String job;
    private String name;
    private String profilePath;

    private CrewMember() {
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

    public static CrewMember constructCrewMember(JSONObject crewObject) throws JSONException {
        CrewMember crewMember = new CrewMember();
//        new CrewMember((int)crewObject.get("id"), job, (String)crewObject.get("name"), (String)crewObject.get("profile_path"));

        if (crewObject.has("id") && crewObject.get("id") instanceof Integer) {
            crewMember.setId((int)crewObject.get("id"));
        } else {
            crewMember.setId(-1);
        }

        crewMember.setJob((String) crewObject.get("job"));


        if (crewObject.has("name") && crewObject.get("name") instanceof String) {
            crewMember.setName((String)crewObject.get("name"));
        } else {
            crewMember.setName(" - ");
        }

        if (crewObject.has("profile_path") && crewObject.get("profile_path") instanceof String) {
            crewMember.setProfilePath((String)crewObject.get("profile_path"));
        } else {
            crewMember.setProfilePath("");
        }

        return crewMember;
    }
}

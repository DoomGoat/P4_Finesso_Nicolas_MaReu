package com.openclassroom.mareu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model object representing a Neighbour
 */
public class Reunion {

    /** Identifier */
    private long id;

    /** Reunion subject */
    private String subject;

    /** Avatar */
    private String avatarColor;

    /** Location */
    private String location;

    /** Time */
    private String time;

    /** Participant list */
    private List<Participant> participants;

    /**
     * Constructor
     * @param id
     * @param subject
     * @param avatarColor
     * @param location
     * @param time
     */

    public Reunion(long id, String subject, String avatarColor, String location, String time, List<Participant> participants) {
        this.id = id;
        this.subject = subject;
        this.avatarColor = avatarColor;
        this.location = location;
        this.time = time;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(String avatarColor) {
        this.avatarColor = avatarColor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

}

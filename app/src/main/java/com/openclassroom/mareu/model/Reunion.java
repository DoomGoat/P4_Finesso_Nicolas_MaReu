package com.openclassroom.mareu.model;

import java.util.List;

/**
 * Model object representing a Neighbour
 */
public class Reunion {

    /** Identifier */
    private long id;

    /** Reunion subject */
    private String name;

    /** Avatar */
    private int avatarColor;

    /** Location */
    private String location;

    /** Time */
    private String time;

    /** Participant list */
    private List<Participant> participants;

    /** Additional information */
    private String info;


    /**
     * Constructor
     * @param id
     * @param name
     * @param avatarColor
     * @param location
     * @param time
     * @param info
     */

    public Reunion(long id, String name, int avatarColor, String location, String time, List<Participant> participants, String info) {
        this.id = id;
        this.name = name;
        this.avatarColor = avatarColor;
        this.location = location;
        this.time = time;
        this.participants = participants;
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarColor() {
        return avatarColor;
    }

    public void setAvatarColor(int avatarColor) {
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}

package com.openclassroom.mareu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

/**
 * Model object representing a Neighbour
 */
public class Reunion implements Parcelable{

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

    /** Date */
    private String date;

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
     * @param date
     * @param info
     */

    public Reunion(long id, String name, int avatarColor, String location, String time, String date, List<Participant> participants, String info) {
        this.id = id;
        this.name = name;
        this.avatarColor = avatarColor;
        this.location = location;
        this.time = time;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(id);
        out.writeString(name);
        out.writeInt(avatarColor);
        out.writeString(location);
        out.writeString(time);
        out.writeString(date);
        //out.writeTypedList(participants);
        out.writeString(info);
    }

    public static final Creator<Reunion> CREATOR = new Creator<Reunion>() {
        @Override
        public Reunion createFromParcel(Parcel in) {
            return new Reunion(in);
        }

        @Override
        public Reunion[] newArray(int size) {
            return new Reunion[size];
        }
    };

    private Reunion(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.avatarColor = in.readInt();
        this.location = in.readString();
        this.time = in.readString();
        this.date = in.readString();
        //in.readTypedList(participants, Participant.CREATOR);
    }

}

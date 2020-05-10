package com.openclassroom.mareu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Room location;

    /** Begin time */
    private Date beginTime;

    /** End time */
    private Date endTime;

    /** Participant list */
    private List<Participant> participants;

    /** Additional information */
    private String info;


    /**
     * Constructor
     */

    public Reunion(long id, String name, int avatarColor, Room location, Date beginTime, Date endTime, List<Participant> participants, String info) {
        this.id = id;
        this.name = name;
        this.avatarColor = avatarColor;
        this.location = location;
        this.beginTime = beginTime;
        this.endTime = endTime;
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

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        out.writeParcelable(location, flags);
        out.writeLong(beginTime.getTime());
        out.writeLong(endTime.getTime());
        out.writeParcelableList(participants, flags);
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
        this.location = in.readParcelable(Room.class.getClassLoader());
        this.beginTime = new Date (in.readLong());
        this.endTime = new Date (in.readLong());
        this.participants = in.readParcelableList(new ArrayList<Participant>(), Participant.class.getClassLoader());
        this.info = in.readString();
    }

}

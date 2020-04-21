package com.openclassroom.mareu.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Participant implements Parcelable {

    /** Identifier */
    private long id;

    /** Email */
    private String email;

    /**
     * Constructor
     * @param id
     * @param email
     */

    public Participant(long id, String email) {
        this.id = id;
        this.email = email;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(email);
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

    private Participant(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
    }


}

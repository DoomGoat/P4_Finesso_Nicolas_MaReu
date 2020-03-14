package com.openclassroom.mareu.model;

public class Participant {

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


}

package com.openclassroom.mareu.events;

import com.openclassroom.mareu.model.Reunion;

public class ClickReunionEvent {

    /**
     * Neighbour clicked
     */
    public Reunion reunion;

    /**
     * Constructor.
     * @param reunion
     */
    public ClickReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}

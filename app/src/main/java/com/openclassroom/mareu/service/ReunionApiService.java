package com.openclassroom.mareu.service;

import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;

import java.util.List;

public interface ReunionApiService {

    /**
     * Get all my Reunions
     * @return {@link List}
     */
    List<Reunion> getReunions();

    /**
     * Deletes a reunion
     */
    void deleteReunion(Reunion reunion);

    /**
     * Create a reunion
     */
    void createReunion(Reunion reunion);

    /**
     * Get all my Rooms
     * @return {@link List}
     */
    List<Room> getRooms();

    /**
     * Get all my Participants
     * @return {@link List}
     */
    List<Participant> getParticipants();

}

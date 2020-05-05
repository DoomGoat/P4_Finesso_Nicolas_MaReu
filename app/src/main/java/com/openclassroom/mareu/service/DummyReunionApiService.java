package com.openclassroom.mareu.service;


import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyReunionApiService implements ReunionApiService {

    private List<Reunion> reunions = DummyReunionGenerator.generateReunion();
    private List<Room> rooms = DummyReunionGenerator.generateRoom();
    private List<Participant> participants = DummyReunionGenerator.generateParticipant();

    @Override
    public List<Reunion> getReunions() {
        return reunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        reunions.remove(reunion);
    }

    @Override
    public void createReunion(Reunion reunion) {
        reunions.add(reunion);
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public List<Participant> getParticipants() {
        return participants;
    }




}

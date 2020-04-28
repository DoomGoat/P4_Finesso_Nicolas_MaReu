package com.openclassroom.mareu.service;

import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;

import java.util.Date;
import java.util.List;

interface ReunionApiServiceB extends ReunionApiService {
    @Override
    List<Reunion> getReunions();

    @Override
    void deleteReunion(Reunion reunion);

    @Override
    void createReunion(Reunion reunion);

    @Override
    List<Participant> getParticipants();

    List<Participant> setReunionParticipants(int... args);

    Date initBeginTime(String time, String date);

    Date initEndTime(String time, String date);
}

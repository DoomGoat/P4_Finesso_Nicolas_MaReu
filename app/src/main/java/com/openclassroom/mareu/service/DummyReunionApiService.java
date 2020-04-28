package com.openclassroom.mareu.service;


import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Dummy mock for the Api
 */
public class DummyReunionApiService implements ReunionApiServiceB {

    private List<Reunion> reunions = DummyReunionGenerator.generateReunion();
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
    public List<Participant> getParticipants() {
        return participants;
    }

    @Override
    public List<Participant> setReunionParticipants(int... args){
        List<Participant> participants = new ArrayList<>();
        for(int x : args) {
            participants.add(DummyReunionGenerator.DUMMY_PARTICIPANTS.get(x));
        }
        return participants;
    }

    @Override
    public Date initBeginTime(String time, String date){
        Date beginTime = null;
        String sDate = date+" "+time;
        try {
            beginTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return beginTime;
    }

    @Override
    public Date initEndTime(String time, String date){
        Date endTime = null;
        String sDate = date+" "+time;
        try {
            endTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endTime;
    }


}

package com.openclassroom.mareu.service;

import android.graphics.Color;

import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyReunionGenerator {

    private static List<Participant> DUMMY_PARTICIPANTS = Arrays.asList(
            new Participant(1, "Caroline@lamzone.com"),
            new Participant(2, "Jack@lamzone.com"),
            new Participant(3, "Chloé@lamzone.com"),
            new Participant(4, "Vincent@lamzone.com"),
            new Participant(5, "Elodie@lamzone.com"),
            new Participant(6, "Sylvain@lamzone.com"),
            new Participant(7, "Laetitia@lamzone.com"),
            new Participant(8, "Dan@lamzone.com"),
            new Participant(9, "Joseph@lamzone.com"),
            new Participant(10, "Emma@lamzone.com"),
            new Participant(11, "Patrick@lamzone.com"),
            new Participant(12, "Martin@lamzone.com"),
            new Participant(13, "Chantal@lamzone.com"),
            new Participant(14, "Eliza@lamzone.com"),
            new Participant(15, "Ludovic@lamzone.com")
    );


    private static List<Reunion> DUMMY_REUNIONS = Arrays.asList(
            new Reunion(1, "Marketing", 0xFF4c4f6a,"Peach", "10:00","01/03/2020", getReunionParticipants(0, 2, 5, 6, 12),""),
            new Reunion(2, "Brainstorming",0xFFd63535,"Mario", "11:00","01/03/2020", getReunionParticipants(1, 4, 8, 3, 14),""),
            new Reunion(3, "Sales",0xFFffee86,"Luigi", "14:30","02/03/2020", getReunionParticipants(0, 5, 10, 13),""),
            new Reunion(4, "HR",0xFF6fd446,"Bowser", "15:30","02/03/2020", getReunionParticipants(3, 7, 14),""),
            new Reunion(5, "Interview",0xFF4690d4, "Mario", "12:00","03/03/2020", getReunionParticipants(9, 11),"")
    );

    private static List<Participant> getReunionParticipants(int ... args){
        List<Participant> participants = new ArrayList<>();
        for(int x : args) {
            participants.add(DUMMY_PARTICIPANTS.get(x));
        }
        return participants;
    }

    static List<Reunion> generateReunion (){
        return new ArrayList<>(DUMMY_REUNIONS);
    }

    static List<Participant> generateParticipant (){
        return new ArrayList<>(DUMMY_PARTICIPANTS);
    }
}



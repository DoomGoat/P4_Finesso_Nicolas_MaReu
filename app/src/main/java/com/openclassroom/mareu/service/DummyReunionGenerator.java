package com.openclassroom.mareu.service;

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
            new Reunion(1, "Marketing","#FFFFFF","Bâtiment A, salle 12", "10h00", getParticipants(0, 2, 5, 6, 12)),
            new Reunion(2, "Brainstorming","#1F1F1F","Bâtiment C, salle 14", "11h00", getParticipants(1, 4, 8, 14, 15)),
            new Reunion(3, "Sales","#2F2F2F","Bâtiment B, salle 18", "14h30", getParticipants(0, 5, 10, 13)),
            new Reunion(4, "HR","#3F3F3F","Bâtiment A, salle 4", "15h30", getParticipants(3, 7, 15)),
            new Reunion(5, "Interview","#4F4F4F","Bâtiment A, salle 12", "16h00", getParticipants(9, 11))
    );

    private static List<Participant> getParticipants (int ... args){
        List<Participant> participants = new ArrayList<>();
        for(int x : args) {
            participants.add(DUMMY_PARTICIPANTS.get(x));
        }
        return participants;
    }

    static List<Reunion> generateReunion (){
        return new ArrayList<>(DUMMY_REUNIONS);
    }
}



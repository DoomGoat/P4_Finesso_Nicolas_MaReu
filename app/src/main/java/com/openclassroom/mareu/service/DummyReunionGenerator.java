package com.openclassroom.mareu.service;

import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class DummyReunionGenerator {

    private static List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room(1, "Mario"),
            new Room(2, "Luigi"),
            new Room(3, "Peach"),
            new Room(4, "Toad"),
            new Room(5, "Bowser")
    );

    public static List<Participant> DUMMY_PARTICIPANTS = Arrays.asList(
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


    public static List<Reunion> DUMMY_REUNIONS = Arrays.asList(
            new Reunion(1, "Marketing", 0xFF4c4f6a, DUMMY_ROOMS.get(1), initBeginTime("10:00", "26/04/2020"), initEndTime("11:00", "26/04/2020"), getReunionParticipants(0, 2, 5, 6, 12),""),
            new Reunion(2, "Brainstorming",0xFFd63535,DUMMY_ROOMS.get(0), initBeginTime("11:00", "26/04/2020"), initEndTime("11:30", "26/04/2020"), getReunionParticipants(1, 4, 8, 3, 14),""),
            new Reunion(3, "Sales",0xFFffee86,DUMMY_ROOMS.get(2), initBeginTime("14:30", "26/04/2020"), initEndTime("15:30", "26/04/2020"), getReunionParticipants(0, 5, 10, 13),""),
            new Reunion(4, "HR",0xFF6fd446, DUMMY_ROOMS.get(4), initBeginTime("12:30", "27/04/2020"), initEndTime("14:00", "27/04/2020"), getReunionParticipants(3, 7, 14),""),
            new Reunion(5, "Interview",0xFF4690d4, DUMMY_ROOMS.get(3), initBeginTime("15:00", "27/04/2020"), initEndTime("16:30", "27/04/2020"), getReunionParticipants(9, 11),""),
            new Reunion(6, "Marketing", 0xFF297045,DUMMY_ROOMS.get(0), initBeginTime("10:00", "27/04/2020"), initEndTime("11:00", "27/04/2020"), getReunionParticipants(0, 2, 5, 6, 12),""),
            new Reunion(7, "Brainstorming",0xFF576675,DUMMY_ROOMS.get(4), initBeginTime("11:00", "28/04/2020"), initEndTime("11:30", "28/04/2020"), getReunionParticipants(1, 4, 8, 3, 14),""),
            new Reunion(8, "Sales",0xFF808080,DUMMY_ROOMS.get(1), initBeginTime("14:30", "28/04/2020"), initEndTime("15:30", "28/04/2020"), getReunionParticipants(0, 5, 10, 13),""),
            new Reunion(9, "HR",0xFFaa3e64,DUMMY_ROOMS.get(0), initBeginTime("12:30", "29/04/2020"), initEndTime("14:00", "29/04/2020"), getReunionParticipants(3, 7, 14),""),
            new Reunion(10, "Interview",0xFFffa500, DUMMY_ROOMS.get(2), initBeginTime("15:00", "29/04/2020"), initEndTime("16:30", "29/04/2020"), getReunionParticipants(9, 11),"")
    );


    private static List<Participant> getReunionParticipants(int... args){
        List<Participant> participants = new ArrayList<>();
        for(int x : args) {
            participants.add(DUMMY_PARTICIPANTS.get(x));
        }
        return participants;
    }

    private static Date initBeginTime(String time, String date){
        Date beginTime = null;
        String sDate = date+" "+time;
        try {
            beginTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return beginTime;
    }

    private static Date initEndTime(String time, String date){
        Date endTime = null;
        String sDate = date+" "+time;
        try {
            endTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endTime;
    }

    static List<Reunion> generateReunion (){
        return new ArrayList<>(DUMMY_REUNIONS);
    }

    static List<Room> generateRoom(){
        return new ArrayList<>(DUMMY_ROOMS);
    }

    public static String [] listRoom () {
        String [] list = new String [DUMMY_ROOMS.size()];
        for (int i = 0 ; i < DUMMY_ROOMS.size(); i++){
            list[i] = DUMMY_ROOMS.get(i).getRoom();
        }
        return list;
    }

    static List<Participant> generateParticipant (){
        return new ArrayList<>(DUMMY_PARTICIPANTS);
    }
}



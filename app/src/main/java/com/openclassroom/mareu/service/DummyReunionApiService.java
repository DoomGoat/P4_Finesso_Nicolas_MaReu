package com.openclassroom.mareu.service;

import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    @Override
    public List<Reunion> reunionListFilter (boolean isDateFiltered, boolean isLocationFiltered, String roomFilterSelected, Date dateFilterSelected) {

        List<Reunion> reunionsArrayList = new ArrayList<>();

        // Filter (if either room, date or both are filtered)
        if (isDateFiltered || isLocationFiltered){
            for (int i = 0; i < getReunions().size(); i++) {

                // Comparison of rooms
                String sReunionLocation = getReunions().get(i).getLocation().getRoom();
                boolean boolLocation = sReunionLocation.equals(roomFilterSelected);

                // Comparison of dates (with calendars)
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(getReunions().get(i).getBeginTime());
                cal2.setTime(dateFilterSelected);

                boolean boolDate = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

                //boolean boolDate = stringOfActualDate.equals(dateFilterSelected);

                // If both are filtered and match filter, add the meeting to the list
                if (isLocationFiltered && isDateFiltered) {
                    if (boolLocation && boolDate) { reunionsArrayList.add(getReunions().get(i)); }

                    // If only the room is filtered and match filter, add the meeting to the list
                } else if (isLocationFiltered) {
                    if (boolLocation) { reunionsArrayList.add(getReunions().get(i)); }

                    // If only the date is filtered and match filter, add the meeting to the list
                } else {
                    if (boolDate) { reunionsArrayList.add(getReunions().get(i)); }
                }
            }
        // Without filter just show all meetings
        } else {
            reunionsArrayList = getReunions();
        }
        return reunionsArrayList;
    }
}




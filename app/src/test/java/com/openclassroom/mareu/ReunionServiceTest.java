package com.openclassroom.mareu;

import com.openclassroom.mareu.di.DI;
import com.openclassroom.mareu.model.Participant;
import com.openclassroom.mareu.model.Reunion;
import com.openclassroom.mareu.model.Room;
import com.openclassroom.mareu.service.DummyReunionGenerator;
import com.openclassroom.mareu.service.ReunionApiService;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;


/**
 * Unit test on Reunion service
 */
@RunWith(JUnit4.class)
public class ReunionServiceTest {

    private ReunionApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getReunionWithSuccess() {
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyReunionGenerator.DUMMY_REUNIONS;
        assertThat(reunions, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(expectedReunions.toArray())));
    }

    @Test
    public void getParticipantWithSuccess() {
        List<Participant> participants = service.getParticipants();
        List<Participant> expectedParticipants = DummyReunionGenerator.DUMMY_PARTICIPANTS;
        assertThat(participants, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(expectedParticipants.toArray())));
    }

    @Test
    public void getRoomWithSuccess() {
        List<Room> rooms = service.getRooms();
        List<Room> expectedRooms = DummyReunionGenerator.DUMMY_ROOMS;
        assertThat(rooms, IsIterableContainingInAnyOrder.containsInAnyOrder(Objects.requireNonNull(expectedRooms.toArray())));
    }


    @Test
    public void deleteReunionWithSuccess() {
        Reunion reunionToDelete = service.getReunions().get(0);
        service.deleteReunion(reunionToDelete);
        assertFalse(service.getReunions().contains(reunionToDelete));
    }

    @Test
    public void createReunionWithSuccess() {
        Reunion reunionToCreate = new Reunion(System.currentTimeMillis(),"",0xFFFFFFFF,service.getRooms().get(0), new Date(), new Date(), service.getParticipants(),"");
        service.createReunion(reunionToCreate);
        assertTrue(service.getReunions().contains(reunionToCreate));
    }

    @Test
    public void filterReunionWithSuccess() {
        int testIndex = 1;
        Reunion expectedReunion = service.getReunions().get(testIndex);
        List<Reunion> filteredReunions = service.reunionListFilter(true, true, expectedReunion.getLocation().getRoom(), expectedReunion.getBeginTime());
        assertTrue(filteredReunions.contains(expectedReunion));
        assertFalse(filteredReunions.size() > 1);
    }

}


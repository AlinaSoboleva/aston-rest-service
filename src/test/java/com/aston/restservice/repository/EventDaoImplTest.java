package com.aston.restservice.repository;

import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.EventDaoImpl;
import com.aston.restservice.repository.impl.UserDaoImpl;
import com.aston.restservice.util.GetProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class EventDaoImplTest extends AbstractBaseDaoTest {

    EventDaoImpl eventDao;

    UserDaoImpl userDao;

    User firstUser;

    User initiator;

    Event firstEvent;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = (UserDaoImpl) GetProvider.getUserDao();
        userDao.setConnectionBuilder(getConnectionBuilder());
        eventDao = (EventDaoImpl) GetProvider.getEventDao();
        eventDao.setConnectionBuilder(getConnectionBuilder());

        firstUser = getFirstUser();

        firstEvent = new Event();
        firstEvent.setTitle(FIRST_EVENT_TITLE);
        firstEvent.setDescription(FIRST_EVENT_DESCRIPTION);

        initiator = userDao.save(firstUser).get();
        firstEvent.setInitiator(initiator);
    }

    @AfterEach
    void ternDown() throws SQLException {
        userDao.deleteById(initiator.getId());
    }

    @Test
    public void saveEvent_usualCase() throws SQLException {
        Optional<Event> savedEventOpt = eventDao.save(firstEvent);

        assertThat(savedEventOpt.isPresent(), equalTo(true));
        Event savedEvent = savedEventOpt.get();

        assertNotNull(savedEvent.getId());
        assertThat(savedEvent.getTitle(), equalTo(FIRST_EVENT_TITLE));
        assertThat(savedEvent.getDescription(), equalTo(FIRST_EVENT_DESCRIPTION));
        assertThat(savedEvent.getInitiatorId(), equalTo(initiator.getId()));

        eventDao.deleteById(savedEvent.getId());
    }

    @Test
    public void saveEvent_whenInitiatorIsNull_throwSQLException() throws SQLException {
        firstEvent.setInitiator(null);

        Exception e = assertThrows(PSQLException.class, () ->
                eventDao.save(firstEvent));

        assertThat(e.getClass(), equalTo(PSQLException.class));
    }

    @Test
    public void saveEvent_whenTitleIsNull_throwSQLException() throws SQLException {
        firstEvent.setTitle(null);

        Exception e = assertThrows(PSQLException.class, () ->
                eventDao.save(firstEvent));

        assertThat(e.getClass(), equalTo(PSQLException.class));
    }

    @Test
    public void updateEventTitle_usualCase() throws SQLException {
        Event savedEvent = eventDao.save(firstEvent).get();
        savedEvent.setTitle(UPDATED_EVENT_TITLE);

        eventDao.update(savedEvent);
        Event updatedEvent = eventDao.findById(savedEvent.getId()).get();

        assertThat(updatedEvent.getTitle(), equalTo(UPDATED_EVENT_TITLE));
        assertThat(updatedEvent.getDescription(), equalTo(firstEvent.getDescription()));

        eventDao.deleteById(savedEvent.getId());
    }

    @Test
    public void findEventById_whenEventNotFount_returnNull() throws SQLException {
        Optional<Event> event = eventDao.findById(FIRST_ID);

        assertFalse(event.isPresent());
    }

    @Test
    public void findEventById_usualCase_returnEvent() throws SQLException {
        Event savedEvent = eventDao.save(firstEvent).get();

        Optional<Event> event = eventDao.findById(savedEvent.getId());

        assertTrue(event.isPresent());
        assertThat(event.get().getTitle(), equalTo(firstEvent.getTitle()));
        assertThat(event.get().getInitiatorId(), equalTo(firstEvent.getInitiatorId()));

        eventDao.deleteById(savedEvent.getId());
    }

    @Test
    public void findAll_usualCase_returnEventList() throws SQLException {
        Event secondEvent = getSecondEvent(initiator);

        Event savedEvent = eventDao.save(firstEvent).get();
        Event savedEvent2 = eventDao.save(secondEvent).get();

        List<Event> events = eventDao.findAll();

        assertThat(events, hasSize(2));
        assertThat(events.get(0).getTitle(), equalTo(savedEvent.getTitle()));

        eventDao.deleteById(savedEvent.getId());
        eventDao.deleteById(savedEvent2.getId());
    }

    @Test
    public void addTwoParticipantsAndDeleteParticipant_usualCase() throws SQLException {
        Event savedEvent = eventDao.save(firstEvent).get();
        User participant =  userDao.save(getSecondUser()).get();
        User participant2 =  userDao.save(getThridUser()).get();

        eventDao.addParticipants(savedEvent.getId(), participant.getId());
        eventDao.addParticipants(savedEvent.getId(), participant2.getId());
        Event eventWithParticipant = eventDao.findById(savedEvent.getId()).get();

        assertThat(eventWithParticipant.getParticipants(),hasSize(2));
        assertTrue(eventWithParticipant.getParticipants().contains(participant));
        assertTrue(eventWithParticipant.getParticipants().contains(participant2));

        eventDao.addParticipants(savedEvent.getId(), participant.getId());

        Event eventWithoutParticipant = eventDao.findById(savedEvent.getId()).get();
        assertThat(eventWithoutParticipant.getParticipants(),hasSize(1));
        assertFalse(eventWithoutParticipant.getParticipants().contains(participant));
        assertTrue(eventWithoutParticipant.getParticipants().contains(participant2));

        eventDao.deleteById(savedEvent.getId());
        userDao.deleteById(participant.getId());
        userDao.deleteById(participant2.getId());
    }
}
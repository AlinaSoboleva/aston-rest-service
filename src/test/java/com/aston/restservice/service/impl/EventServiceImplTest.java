package com.aston.restservice.service.impl;

import com.aston.restservice.dto.EventResponseDto;
import com.aston.restservice.dto.EventShortDto;
import com.aston.restservice.exception.ConflictException;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.mapper.EventMapper;
import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.EventDaoImpl;
import com.aston.restservice.util.GetProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static com.aston.restservice.testUtil.TestGetProvider.getEvent;
import static com.aston.restservice.testUtil.TestGetProvider.getUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventDaoImpl eventDao;

    @InjectMocks
    private EventServiceImpl eventService;

    private User initiator;

    @BeforeEach
    void setUp() {
        initiator = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
    }

    @Test
    void saveEvent_whenEventIdIsNull_returnSavedEvent() throws SQLException {
        initiator.setId(FIRST_ID);
        Event expected = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);
        when(eventDao.save(any(Event.class))).thenReturn(Optional.of(expected));

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            EventResponseDto actual = eventService.saveEvent(EventMapper.toDto(expected), FIRST_ID);

            assertThat(actual.getTitle(), equalTo(expected.getTitle()));
            verify(eventDao, times(1)).save(any(Event.class));
            verify(eventDao, never()).update(any(Event.class));
        }
    }

    @Test
    void saveEvent_whenEventTitleIsNull_throwException() throws SQLException {
        initiator.setId(FIRST_ID);
        Event expected = getEvent(null, FIRST_EVENT_DESCRIPTION, initiator);
        when(eventDao.save(any(Event.class))).thenReturn(Optional.empty());

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            Exception exception = assertThrows(Exception.class, () ->
                    eventService.saveEvent(EventMapper.toDto(expected), FIRST_ID));

            assertThat(exception.getClass(), equalTo(HttpException.class));
            verify(eventDao, times(1)).save(any(Event.class));
            verify(eventDao, never()).update(any(Event.class));
        }
    }

    @Test
    void saveEvent_whenEventIdIsNotNull_returnUpdatedEvent() throws SQLException {
        initiator.setId(FIRST_ID);
        Event updatedEvent = getEvent(UPDATED_EVENT_TITLE, null, initiator);
        updatedEvent.setId(FIRST_ID);
        Event event = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);
        event.setId(FIRST_ID);
        Mockito.doNothing().when(eventDao).update(any(Event.class));

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(event);
            EventResponseDto actual = eventService.saveEvent(EventMapper.toDto(updatedEvent), FIRST_ID);

            assertThat(actual.getTitle(), equalTo(updatedEvent.getTitle()));
            verify(eventDao, times(1)).update(any(Event.class));
            verify(eventDao, never()).save(any(Event.class));
        }
    }

    @Test
    void updateEvent_whenUserIsNotInitiator_throwException() throws SQLException {
        initiator.setId(FIRST_ID);
        Event updatedEvent = getEvent(UPDATED_EVENT_TITLE, null, initiator);
        updatedEvent.setId(FIRST_ID);
        Event event = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);
        event.setId(FIRST_ID);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(event);
            Exception exception = assertThrows(Exception.class, () ->
                    eventService.saveEvent(EventMapper.toDto(updatedEvent), SECOND_ID));

            assertThat(exception.getClass(), equalTo(ConflictException.class));
            verify(eventDao, never()).update(any(Event.class));
            verify(eventDao, never()).save(any(Event.class));
        }
    }

    @Test
    void getAllEvents_whereRequestPathIsNull() throws SQLException {
        List<Event> expected = List.of(
                getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator),
                getEvent(SECOND_EVENT_TITLE, SECOND_EVENT_DESCRIPTION, initiator));

        when(eventDao.findAll()).thenReturn(expected);

        List<EventShortDto> actual = eventService.getEvents(null);

        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        verify(eventDao, times(1)).findAll();
    }

    @Test
    void getAllEvents_whereRequestPathIsNotNull_returnEventById() throws SQLException {
        Event expected = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEntityId(anyString())).thenReturn(FIRST_ID);
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(expected);
            List<EventShortDto> actual = eventService.getEvents("requestPath");

            assertThat(actual, hasSize(1));
            assertThat(actual.get(0).getTitle(), equalTo(FIRST_EVENT_TITLE));
            verify(eventDao, never()).findAll();
        }
    }

    @Test
    void deleteEvent() throws SQLException {
        initiator.setId(FIRST_ID);
        Event expected = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEntityId(anyString())).thenReturn(FIRST_ID);
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(expected);
            doNothing().when(eventDao).deleteById(anyLong());

            Long deletedEventId = eventService.deleteEvent("requestPath", FIRST_ID);

            assertThat(deletedEventId, equalTo(FIRST_ID));
            verify(eventDao, times(1)).deleteById(anyLong());
        }
    }

    @Test
    void addOneParticipantTwoTimes_firstTimeWillAddAndSecondTimeWillDelete() throws SQLException {
        initiator.setId(FIRST_ID);
        Event expected = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(getUser(SECOND_USER_NAME, SECOND_USER_EMAIL));
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(expected);
            when(eventDao.addParticipants(anyLong(), anyLong())).thenReturn(true);

            EventResponseDto actual = eventService.addParticipant(FIRST_ID, SECOND_ID);

            assertThat(actual.getParticipants(), hasSize(1));
            assertThat(actual.getParticipants().iterator().next().getName(), equalTo(SECOND_USER_NAME));

            when(eventDao.addParticipants(anyLong(), anyLong())).thenReturn(false);

            actual = eventService.addParticipant(FIRST_ID, SECOND_ID);
            assertThat(actual.getParticipants(), hasSize(0));
        }
    }

}
package com.aston.restservice.util;

import com.aston.restservice.exception.ConflictException;
import com.aston.restservice.exception.EntityNotFoundException;
import com.aston.restservice.model.Event;
import com.aston.restservice.repository.ContactDao;
import com.aston.restservice.repository.EventDao;
import com.aston.restservice.repository.UserDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProviderTest {

    @Mock
    private UserDao userDao;

    @Mock
    private EventDao eventDao;

    @Mock
    private ContactDao contactDao;

    @Test
    void getUser_whenUserNotFound_throwException() throws SQLException {
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class, CALLS_REAL_METHODS)) {
            getProvider.when(GetProvider::getUserDao).thenReturn(userDao);
            when(userDao.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () ->
                    GetProvider.getUser(FIRST_ID));

            assertEquals(exception.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    void getEvent_whenEventNotFound_throwException() throws SQLException {
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class, CALLS_REAL_METHODS)) {
            getProvider.when(GetProvider::getEventDao).thenReturn(eventDao);
            when(eventDao.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () ->
                    GetProvider.getEvent(FIRST_ID));

            assertEquals(exception.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    void getContact_whenContactNotFound_throwException() throws SQLException {
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class, CALLS_REAL_METHODS)) {
            getProvider.when(GetProvider::getContactDao).thenReturn(contactDao);
            when(contactDao.findById(anyLong())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () ->
                    GetProvider.getContact(FIRST_ID));

            assertEquals(exception.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    void getEvent_whenEventFound_returnEvent() throws SQLException {
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class, CALLS_REAL_METHODS)) {
            getProvider.when(GetProvider::getEventDao).thenReturn(eventDao);
            Event expected = new Event();
            expected.setTitle(FIRST_EVENT_TITLE);
            when(eventDao.findById(anyLong())).thenReturn(Optional.of(expected));

            Event actual = GetProvider.getEvent(FIRST_ID);

            assertEquals(actual.getTitle(), expected.getTitle());
        }
    }

    @Test
    void getObjectMapper() {
        ObjectMapper objectMapper = GetProvider.getObjectMapper();

        assertEquals(objectMapper.getClass(), ObjectMapper.class);
    }

    @Test
    void getEntityId_whenPathIsValid_returnEntityId() {
        String requestPath = "/" + FIRST_ID;

        Long actualId = GetProvider.getEntityId(requestPath);

        assertEquals(FIRST_ID, actualId);
    }

    @Test
    void getEntityId_whenPathIsIncorrect_throwException() {
        Exception exception = assertThrows(Exception.class, () ->
                GetProvider.getEntityId(INCORRECT_PATH));

        assertEquals(exception.getClass(), ConflictException.class);
    }

    @Test
    void getEntityId_whenPathIsNull_throwException() {
        String requestPath = null;

        Exception exception = assertThrows(Exception.class, () ->
                GetProvider.getEntityId(requestPath));

        assertEquals(exception.getClass(), ConflictException.class);
    }
}
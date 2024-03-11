package com.aston.restservice.util;

import com.aston.restservice.exception.EntityNotFoundException;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.ContactDao;
import com.aston.restservice.repository.EventDao;
import com.aston.restservice.repository.UserDao;
import com.aston.restservice.repository.impl.ContactDaoImpl;
import com.aston.restservice.repository.impl.EventDaoImpl;
import com.aston.restservice.repository.impl.UserDaoImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class GetProvider {

    private GetProvider() {
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static UserDao getUserDao() {
        return UserDaoImpl.getInstance();
    }

    public static EventDao getEventDao() {
        return EventDaoImpl.getInstance();
    }

    public static ContactDao getContactDao() {
        return ContactDaoImpl.getInstance();
    }

    public static User getUser(Long id) throws SQLException {
        return getUserDao().findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id %d not found", id)));
    }

    public static Event getEvent(Long id) throws SQLException {
        return getEventDao().findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Event with id %d not found", id)));
    }

    public static Contact getContact(Long id) throws SQLException {
        return getContactDao().findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Contact with id %d not found", id)));
    }

    public static Long getEntityId(String requestPath) {
        String[] pathArray = requestPath.split("/");
        return Long.parseLong(pathArray[1]);
    }

    public static String getBody(HttpServletRequest req) {
        try {
            req.setCharacterEncoding(Constants.CHARACTER_ENCODING);
            return req.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        }
    }
}

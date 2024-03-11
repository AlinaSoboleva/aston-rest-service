package com.aston.restservice.service;

import com.aston.restservice.dto.ContactDto;

import java.sql.SQLException;

public interface ContactService {
    ContactDto saveContact(ContactDto contactDto, Long eventId, Long userId) throws SQLException;

    Long deleteContact(Long contactId, Long eventId, Long userId) throws SQLException;
}

package com.aston.restservice.repository;

import com.aston.restservice.model.Contact;

import java.sql.SQLException;
import java.util.Optional;

public interface ContactDao {

    void update(Contact contact) throws SQLException;

    Optional<Contact> save(Contact contact, Long eventId) throws SQLException;

    Optional<Contact> findById(Long id) throws SQLException;

    void deleteById(Long id) throws SQLException;
}

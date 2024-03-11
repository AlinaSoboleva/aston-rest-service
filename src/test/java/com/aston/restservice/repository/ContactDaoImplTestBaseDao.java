package com.aston.restservice.repository;

import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.repository.impl.ContactDaoImpl;
import com.aston.restservice.util.GetProvider;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static com.aston.restservice.testUtil.TestGetProvider.getContact;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ContactDaoImplTestBaseDao extends EventDaoImplTestBaseDao {

    ContactDaoImpl contactDao;

    Contact contact;
    Event savedEvent;

    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        contactDao = (ContactDaoImpl) GetProvider.getContactDao();
        contactDao.setConnectionBuilder(getConnectionBuilder());
        savedEvent = eventDao.save(firstEvent).get();
        contact = getContact(CONTACT_PHONE, CONTACT_ADDRESS,savedEvent.getId());
    }

    @Test
    void saveContact_usualCase() throws SQLException {
        Contact savedContact = contactDao.save(contact, savedEvent.getId()).get();

        Event eventWithContact = eventDao.findById(savedEvent.getId()).get();

        assertNotNull(eventWithContact.getContact());
        assertThat(eventWithContact.getContact().getId(), equalTo(savedContact.getId()));
    }

    @Test
    void updateContact_usualCase() throws SQLException {
        Contact savedContact = contactDao.save(contact, savedEvent.getId()).get();
        savedContact.setAddress(UPDATED_CONTACT_ADDRESS);

        contactDao.update(savedContact);
        Contact updatedContact = contactDao.findById(savedContact.getId()).get();

        assertThat(updatedContact.getAddress(), equalTo(UPDATED_CONTACT_ADDRESS));
        assertThat(updatedContact.getPhone(), equalTo(savedContact.getPhone()));
    }

    @Test
    void findById_whenContactNotFound_returnNull() throws SQLException {
        Optional<Contact> result = contactDao.findById(FIRST_ID);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_usualCase() throws SQLException {
        Contact savedContact = contactDao.save(contact, savedEvent.getId()).get();
        contactDao.deleteById(savedContact.getId());
        Optional<Contact> result = contactDao.findById(FIRST_ID);

        assertFalse(result.isPresent());

    }

    @Ignore
    public void saveEvent_usualCase() throws SQLException {
        super.saveEvent_usualCase();
    }

    @Ignore
    public void saveEvent_whenInitiatorIsNull_throwSQLException() throws SQLException {
        super.saveEvent_whenInitiatorIsNull_throwSQLException();
    }

    @Ignore
    public void saveEvent_whenTitleIsNull_throwSQLException() throws SQLException {
        super.saveEvent_whenTitleIsNull_throwSQLException();
    }

    @Ignore
    public void updateEventTitle_usualCase() throws SQLException {
        super.updateEventTitle_usualCase();
    }

    @Ignore
    public void findEventById_whenEventNotFount_returnNull() throws SQLException {
        super.findEventById_whenEventNotFount_returnNull();
    }

    @Ignore
    public void findEventById_usualCase_returnEvent() throws SQLException {
        super.findEventById_usualCase_returnEvent();
    }

    @Ignore
    public void findAll_usualCase_returnEventList() throws SQLException {
        super.findAll_usualCase_returnEventList();
    }

    @Ignore
    public void addTwoParticipantsAndDeleteParticipant_usualCase() throws SQLException {
        super.addTwoParticipantsAndDeleteParticipant_usualCase();
    }
}

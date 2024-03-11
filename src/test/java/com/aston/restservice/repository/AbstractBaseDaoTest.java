package com.aston.restservice.repository;

import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.ConnectionBuilderImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.aston.restservice.testData.TestConstants.*;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractBaseDaoTest {

    protected final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    protected ConnectionBuilder connectionBuilder;

    {
        postgres.withInitScript("test.sql");
        postgres.withPassword("postgres");
        postgres.withUsername("postgres");
        connectionBuilder = new ConnectionBuilderImpl();
    }

    protected ConnectionBuilderImpl getConnectionBuilder() {
        return new ConnectionBuilderImpl(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword(), postgres.getDriverClassName());
    }

    @BeforeAll
    void start() {
        postgres.start();
    }

    @AfterAll
    void stop() {
        postgres.stop();
    }

    protected Event getSecondEvent(User initiator) {
        Event secondEvent = new Event();
        secondEvent.setTitle(SECOND_EVENT_TITLE);
        secondEvent.setDescription(SECOND_EVENT_DESCRIPTION);
        secondEvent.setInitiator(initiator);
        return secondEvent;
    }

    protected User getFirstUser() {
        User secondUser = new User();
        secondUser.setName(FIRST_USER_NAME);
        secondUser.setEmail(FIRST_USER_EMAIL);

        return secondUser;
    }

    protected User getSecondUser() {
        User secondUser = new User();
        secondUser.setName(SECOND_USER_NAME);
        secondUser.setEmail(SECOND_USER_EMAIL);

        return secondUser;
    }

    protected User getThridUser() {
        User secondUser = new User();
        secondUser.setName(THIRD_USER_NAME);
        secondUser.setEmail(THIRD_USER_EMAIL);

        return secondUser;
    }

    protected Contact getContact(Long eventId) {
        Contact contact = new Contact();
        contact.setPhone(CONTACT_PHONE);
        contact.setAddress(CONTACT_ADDRESS);
        contact.setEventId(eventId);

        return contact;
    }
}

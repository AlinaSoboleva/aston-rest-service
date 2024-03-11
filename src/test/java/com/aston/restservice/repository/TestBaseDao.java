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
public class TestBaseDao {

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
}

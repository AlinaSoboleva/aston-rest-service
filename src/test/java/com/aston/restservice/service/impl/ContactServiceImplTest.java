package com.aston.restservice.service.impl;

import com.aston.restservice.dto.ContactDto;
import com.aston.restservice.mapper.ContactMapper;
import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.ContactDaoImpl;
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
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static com.aston.restservice.testUtil.TestGetProvider.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private ContactDaoImpl contactDao;

    @InjectMocks
    private ContactServiceImpl contactService;

    private User initiator;
    private Event event;

    @BeforeEach
    void setUp() {
        initiator = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
        initiator.setId(FIRST_ID);

        event = getEvent(FIRST_EVENT_TITLE, FIRST_EVENT_DESCRIPTION, initiator);
        event.setId(FIRST_ID);
    }

    @Test
    void saveContact_whenEventDoesNotContainContact_returnSavedContact() throws SQLException {
        Contact expected = getContact(CONTACT_PHONE, CONTACT_ADDRESS, event.getId());

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(event);
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            when(contactDao.save(any(Contact.class), anyLong())).thenReturn(Optional.of(expected));

            ContactDto actual = contactService.saveContact(ContactMapper.toDto(expected), FIRST_ID, FIRST_ID);

            assertThat(actual.getAddress(), equalTo(expected.getAddress()));
            verify(contactDao, times(1)).save(any(Contact.class), anyLong());
            verify(contactDao, never()).update(any(Contact.class));
        }
    }

    @Test
    void saveContact_whenEventContainContact_returnUpdatedContact() throws SQLException {
        Contact expected = getContact(CONTACT_PHONE, CONTACT_ADDRESS, event.getId());
        expected.setId(FIRST_ID);
        expected.setEventId(event.getId());
        event.setContact(expected);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(event);
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            getProvider.when(() -> GetProvider.getContact(anyLong())).thenReturn(expected);
            doNothing().when(contactDao).update(any(Contact.class));

            ContactDto actual = contactService.saveContact(ContactMapper.toDto(getContact(null, UPDATED_CONTACT_ADDRESS, event.getId())), FIRST_ID, FIRST_ID);

            assertThat(actual.getAddress(), equalTo(UPDATED_CONTACT_ADDRESS));
            verify(contactDao, times(1)).update(any(Contact.class));
            verify(contactDao, never()).save(any(Contact.class), anyLong());
        }
    }

    @Test
    void deleteContact() throws SQLException {
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEvent(anyLong())).thenReturn(event);
            getProvider.when(() -> GetProvider.getUser(anyLong())).thenReturn(initiator);
            doNothing().when(contactDao).deleteById(anyLong());

            Long deletedContactId = contactService.deleteContact(FIRST_ID, FIRST_ID, FIRST_ID);

            assertThat(deletedContactId, equalTo(FIRST_ID));
            verify(contactDao, times(1)).deleteById(anyLong());
        }
    }
}
package com.aston.restservice.service.impl;

import com.aston.restservice.dto.ContactDto;
import com.aston.restservice.mapper.ContactMapper;
import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.repository.ContactDao;
import com.aston.restservice.service.ContactService;
import com.aston.restservice.util.GetProvider;
import com.aston.restservice.util.Validator;

import java.sql.SQLException;
import java.util.Optional;

public class ContactServiceImpl implements ContactService {

    private static ContactServiceImpl INSTANCE;

    private static ContactDao contactDao;

    private ContactServiceImpl() {
    }

    public static synchronized ContactServiceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContactServiceImpl();
            contactDao = GetProvider.getContactDao();
        }
        return INSTANCE;
    }

    @Override
    public ContactDto saveContact(ContactDto contactDto, Long eventId, Long userId) throws SQLException {
        Event event = GetProvider.getEvent(eventId);
        GetProvider.getUser(userId);
        Validator.checkEventInitiator(event, userId);
        Contact savedContact = null;
        if (event.getContactId() == 0) {
            Contact contact = ContactMapper.toEntity(contactDto, eventId);
            Optional<Contact> optional = contactDao.save(contact, eventId);
            if (optional.isPresent()) savedContact = optional.get();
        } else {
            savedContact = GetProvider.getContact(event.getContactId());
            savedContact.setAddress(contactDto.getAddress() == null ? savedContact.getAddress() : contactDto.getAddress());
            savedContact.setPhone(contactDto.getPhone() == null ? savedContact.getPhone() : contactDto.getPhone());
            contactDao.update(savedContact);
        }
        return ContactMapper.toDto(savedContact);
    }

    @Override
    public Long deleteContact(Long contactId, Long eventId, Long userId) throws SQLException {
        GetProvider.getUser(userId);
        Event event = GetProvider.getEvent(eventId);
        Validator.checkEventInitiator(event, userId);
        contactDao.deleteById(contactId);
        return contactId;
    }
}

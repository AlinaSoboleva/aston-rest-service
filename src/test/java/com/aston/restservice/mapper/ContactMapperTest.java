package com.aston.restservice.mapper;

import com.aston.restservice.dto.ContactDto;
import com.aston.restservice.model.Contact;
import org.junit.jupiter.api.Test;

import static com.aston.restservice.testData.TestConstants.*;
import static com.aston.restservice.testUtil.TestGetProvider.getContact;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.equalTo;

class ContactMapperTest {

    @Test
    void toDto() {
        Contact contact = getContact(CONTACT_PHONE, CONTACT_ADDRESS, FIRST_ID);

        ContactDto actual = ContactMapper.toDto(contact);

        assertThat(actual.getPhone(), equalTo(contact.getPhone()));
        assertThat(actual.getAddress(), equalTo(contact.getAddress()));
    }

    @Test
    void toEntity() {
        ContactDto contactDto = ContactDto.builder()
                .id(FIRST_ID)
                .phone(CONTACT_PHONE)
                .address(CONTACT_ADDRESS)
                .build();

        Contact actual = ContactMapper.toEntity(contactDto, FIRST_ID);

        assertThat(actual.getPhone(), equalTo(contactDto.getPhone()));
        assertThat(actual.getAddress(), equalTo(contactDto.getAddress()));
        assertThat(actual.getEventId(), equalTo(FIRST_ID));
    }
}
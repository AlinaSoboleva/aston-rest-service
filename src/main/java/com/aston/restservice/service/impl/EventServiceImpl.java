package com.aston.restservice.service.impl;

import com.aston.restservice.dto.EventDto;
import com.aston.restservice.dto.EventResponseDto;
import com.aston.restservice.dto.EventShortDto;
import com.aston.restservice.dto.UserDto;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.mapper.EventMapper;
import com.aston.restservice.mapper.UserMapper;
import com.aston.restservice.model.Contact;
import com.aston.restservice.model.Event;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.EventDao;
import com.aston.restservice.service.EventService;
import com.aston.restservice.util.GetProvider;
import com.aston.restservice.util.Validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventServiceImpl implements EventService {

    private static EventServiceImpl INSTANCE;

    private static EventDao eventDao;

    private EventServiceImpl() {
    }

    public static synchronized EventServiceImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventServiceImpl();
            eventDao = GetProvider.getEventDao();
        }
        return INSTANCE;
    }

    @Override
    public EventResponseDto saveEvent(EventDto eventDto, Long userId) throws SQLException {
        User initiator = GetProvider.getUser(userId);
        Event savedEvent;
        if (eventDto.getId() == null) {
            Event event = EventMapper.toEntity(eventDto, initiator);
            savedEvent = eventDao.save(event).orElseThrow(() ->
                    new HttpException("Event was not saved " + eventDto));
        } else {
            savedEvent = GetProvider.getEvent(eventDto.getId());
            savedEvent.setTitle(eventDto.getTitle() == null ? savedEvent.getTitle() : eventDto.getTitle());
            savedEvent.setDescription(eventDto.getDescription() == null ? savedEvent.getDescription() : eventDto.getDescription());
            eventDao.update(savedEvent);
        }
        return EventMapper.toResponseDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getAllEvents(String requestPath) throws SQLException {
        List<Event> events = new ArrayList<>();
        if (requestPath != null) {
            Long id = GetProvider.getEntityId(requestPath);
            Event event = GetProvider.getEvent(id);
            events.add(event);
        } else {
            events = eventDao.findAll();
        }
        return events.stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    public Long deleteEvent(String requestPath, Long userId) throws SQLException {
        Long id = GetProvider.getEntityId(requestPath);
        Event event = GetProvider.getEvent(id);
        Validator.checkEventInitiator(event, userId);
        Contact contact = event.getContact();
        eventDao.deleteById(id);

        return id;
    }

    @Override
    public EventResponseDto addParticipant(Long eventId, Long userId) throws SQLException {
        Event event = GetProvider.getEvent(eventId);
        User user = GetProvider.getUser(userId);
        boolean isAdd = eventDao.addParticipants(eventId, userId);
        Set<UserDto> participants = event.getParticipants()
                .stream().map(UserMapper::toDto).collect(Collectors.toSet());

        if (isAdd) {
            participants.add(UserMapper.toDto(user));
        } else {
            participants.remove(UserMapper.toDto(user));
        }

        return EventMapper.toResponseDto(event, participants);
    }
}

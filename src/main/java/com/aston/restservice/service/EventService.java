package com.aston.restservice.service;

import com.aston.restservice.dto.EventDto;
import com.aston.restservice.dto.EventResponseDto;
import com.aston.restservice.dto.EventShortDto;

import java.sql.SQLException;
import java.util.List;

public interface EventService {

    EventResponseDto saveEvent(EventDto userDto, Long userId) throws SQLException;

    List<EventShortDto> getAllEvents(String requestPath) throws SQLException;

    Long deleteEvent(String requestPath, Long userId) throws SQLException;

    EventResponseDto addParticipant(Long eventId, Long userId) throws SQLException;
}

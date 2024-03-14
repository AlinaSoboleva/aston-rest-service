package com.aston.restservice.service;

import com.aston.restservice.dto.EventDto;
import com.aston.restservice.dto.EventResponseDto;
import com.aston.restservice.dto.EventShortDto;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс отвечающий за работу с событиями.
 */
public interface EventService {

    /**
     * Сохраняет событие в таблице. <br> Если данное событие уже существует, то обновляет его.
     *
     * @param eventDto сохраняемые данные
     * @param userId   идентификатор пользователя, создающего событие
     * @return EventResponseDto - dto для отображения у клиенту
     * @throws SQLException
     */
    EventResponseDto saveEvent(EventDto eventDto, Long userId) throws SQLException;

    /**
     * Возвращает список всех событий или событие по идентификатору
     *
     * @param requestPath содержит идентификатор события, если необходимо вернуть конкретное событие
     * @return List
     * @throws SQLException
     */
    List<EventShortDto> getEvents(String requestPath) throws SQLException;

    /**
     * Удаляет событие, если пользователь, создавший сопрос является инициатором события
     *
     * @param requestPath путь запроса содержащий идентификатор события
     * @param userId      идентификатор пользователя, создавшего запрос
     * @return Long - идентификатор удаленного события
     * @throws SQLException
     */
    Long deleteEvent(String requestPath, Long userId) throws SQLException;

    /**
     * Добавляет участника события.
     *
     * @param eventId событие, которому добавится участник
     * @param userId  пользователь, которые будет учавствовать в событии
     * @return EventResponseDto
     * @throws SQLException
     */
    EventResponseDto addParticipant(Long eventId, Long userId) throws SQLException;
}

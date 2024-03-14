package com.aston.restservice.service;

import com.aston.restservice.dto.ContactDto;

import java.sql.SQLException;

/**
 * Интерфейс отвечающий за работу с контактной информацией.
 */
public interface ContactService {

    /**
     * Добавляет контактную информацию события. <br> Если данная информация уже существует, то обновляет его.
     *
     * @param contactDto сохраняемые данные
     * @param eventId    идентификатор события, для которого добавляется информация
     * @param userId     идентификатор пользователя, добавляющего информацию
     * @return ContactDto - dto для отображения у клиенту
     * @throws SQLException
     */
    ContactDto saveContact(ContactDto contactDto, Long eventId, Long userId) throws SQLException;

    /**
     * Удаляет контактную информацию у события.
     *
     * @param contactId идентификатор удаляемой информации
     * @param eventId   идентификатор события, содержащего данную информацию
     * @param userId    идентификатор пользователя, удаляющего информацию
     * @return Long - идентификатор удаленной информации
     * @throws SQLException
     */
    Long deleteContact(Long contactId, Long eventId, Long userId) throws SQLException;
}

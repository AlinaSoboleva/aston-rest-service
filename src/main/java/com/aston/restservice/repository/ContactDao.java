package com.aston.restservice.repository;

import com.aston.restservice.model.Contact;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Интерфейс отвечающий за работу с таблицей Contact.
 */
public interface ContactDao {

    /**
     * Обновляет данные в таблице
     *
     * @throws SQLException
     */
    void update(Contact contact) throws SQLException;

    /**
     * Сохраняет данные в таблицу
     *
     * @param contact - сущность для сохранения
     * @param eventId - ID сущности события, к которому относится данная контактная информация
     * @return Optional
     * @throws SQLException
     */
    Optional<Contact> save(Contact contact, Long eventId) throws SQLException;

    /**
     * Получение сущности по ее идентификатору
     *
     * @param id - идентификатор сущности
     * @return Optional
     * @throws SQLException
     */
    Optional<Contact> findById(Long id) throws SQLException;

    /**
     * Удаляет сущность из таблицы.
     *
     * @param id - идентификатор удаляемой сущности
     * @throws SQLException
     */
    void deleteById(Long id) throws SQLException;
}

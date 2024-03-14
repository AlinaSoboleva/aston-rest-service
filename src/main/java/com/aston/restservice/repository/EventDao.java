package com.aston.restservice.repository;

import com.aston.restservice.model.Event;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс отвечающий за связь и работу с таблицей Events.
 */
public interface EventDao {

    /**
     * Обновление информации о событии в БД.
     *
     * @param event  сами данные
     * @throws SQLException
     */
    void update(Event event) throws SQLException;

    /**
     * Сохранение события в БД.
     *
     * @param event  сохраняемые данные
     * @return Optional - сохраненные данные
     * @throws SQLException
     */
    Optional<Event> save(Event event) throws SQLException;

    /**
     * Получение события по идентификатору
     *
     * @param id  идентификатор события
     * @return Optional - полученные данные
     * @throws SQLException
     */
    Optional<Event> findById(Long id) throws SQLException;

    /**
     * Получение списка всех событий
     *
     * @return List
     * @throws SQLException
     */
    List<Event> findAll() throws SQLException;

    /**
     * Удаление какого конкретного события
     *
     * @param id  идентификатор конкретного события
     * @throws SQLException
     */
    void deleteById(Long id) throws SQLException;

    /**
     * Добавление участника события.<br>
     * Если участник уже добавлен, удаляет его
     *
     * @param eventId  идентификатор события
     * @param userId  идентификатор участника
     * @return boolean  <p>true - участник добавлен <br>
     *                   false - участник удален) </p>
     * @throws SQLException
     */
    boolean addParticipants(Long eventId, Long userId) throws SQLException;
}

package com.aston.restservice.repository;

import com.aston.restservice.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс реализующий связь с таблицей Users
 */
public interface UserDao {

    /**
     * Обновляет пользователя
     *
     * @param user данные для обновления
     * @throws SQLException
     */
    void update(User user) throws SQLException;

    /**
     * Сохранение пользователя в БД.
     *
     * @param user сохраняемые данные
     * @return Optional - сохраненные данные
     * @throws SQLException
     */
    Optional<User> save(User user) throws SQLException;

    Optional<User> findById(Long id) throws SQLException;

    List<User> findAll() throws SQLException;

    void deleteById(Long id) throws SQLException;
}

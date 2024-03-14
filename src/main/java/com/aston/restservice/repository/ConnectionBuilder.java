package com.aston.restservice.repository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Интерфейс отвечающий за создание соединения.
 *
 * @see Connection
 */
public interface ConnectionBuilder {
    /**
     * Считывает конфигурационный файл и на его основа создает соединение с БД.
     *
     * @return Connection (соединения с конкретной базой данных)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    Connection getConnection() throws SQLException, ClassNotFoundException;
}

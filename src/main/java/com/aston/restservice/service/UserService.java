package com.aston.restservice.service;

import com.aston.restservice.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface UserService {

    /**
     * Сохраняет пользователя в таблице. <br> Если данный пользователь уже существует, то обновляет его.
     *
     * @param userDto сохраняемые данные
     * @return UserDto - dto для отображения у клиенту
     * @throws SQLException
     */
    UserDto saveUser(UserDto userDto) throws SQLException;

    /**
     * Получение списка всех пользователей
     *
     * @return List - список всех пользователей
     * @throws SQLException
     */
    List<UserDto> getAllUsers() throws SQLException;

    /**
     * Удаление конкретного пользователя
     *
     * @param requestPath пусть запроса, содержащий идентификатор
     * @return Long - идентификатор удаленного пользователя
     * @throws SQLException
     */
    Long deleteUser(String requestPath) throws SQLException;
}

package com.aston.restservice.service.impl;

import com.aston.restservice.dto.UserDto;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.mapper.UserMapper;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.UserDao;
import com.aston.restservice.service.UserService;
import com.aston.restservice.util.GetProvider;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public UserDto saveUser(UserDto userDto) throws SQLException {
        User savedUser;
        if (userDto.getId() == null) {
            User user = UserMapper.toEntity(userDto);
            savedUser = userDao.save(user).orElseThrow(() ->
                    new HttpException("User was not saved " + userDto));
        } else {
            savedUser = GetProvider.getUser(userDto.getId());
            savedUser.setName(userDto.getName() == null ? savedUser.getName() : userDto.getName());
            savedUser.setEmail(userDto.getEmail() == null ? savedUser.getEmail() : userDto.getEmail());
            userDao.update(savedUser);
        }
        return UserMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() throws SQLException {
        List<User> users = userDao.findAll();
        return users.stream().map(UserMapper::toDto).toList();
    }

    @Override
    public Long deleteUser(String requestPath) throws SQLException {
        Long id = GetProvider.getEntityId(requestPath);
        GetProvider.getUser(id);
        userDao.deleteById(id);
        return id;
    }
}

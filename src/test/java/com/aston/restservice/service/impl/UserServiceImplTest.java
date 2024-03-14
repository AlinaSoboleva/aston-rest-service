package com.aston.restservice.service.impl;

import com.aston.restservice.dto.UserDto;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.mapper.UserMapper;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.UserDaoImpl;
import com.aston.restservice.util.GetProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static com.aston.restservice.testUtil.TestGetProvider.getUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDaoImpl userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUser_whenUserIdIsNull_returnSavedUser() throws SQLException {
        User expected = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
        when(userDao.save(any(User.class))).thenReturn(Optional.of(expected));

        UserDto actual = userService.saveUser(UserMapper.toDto(expected));

        assertThat(actual.getName(), equalTo(expected.getName()));
        assertThat(actual.getEmail(), equalTo(expected.getEmail()));
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void saveUser_whenUserNameIsNull_throwException() throws SQLException {
        User expected = getUser(null, FIRST_USER_EMAIL);
        when(userDao.save(any(User.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                userService.saveUser(UserMapper.toDto(expected)));

        assertThat(exception.getClass(), equalTo(HttpException.class));
        assertTrue(exception.getMessage().contains("User was not saved"));
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void saveUser_whenUserIdIsNotNull_returnUpdatedUser() throws SQLException {
        User updatedUser = getUser(UPDATED_USER_NAME, UPDATED_USER_EMAIL);
        updatedUser.setId(FIRST_ID);

        User user = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
        user.setId(FIRST_ID);

        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getUser(FIRST_ID)).thenReturn(user);

            doNothing().when(userDao).update(any(User.class));
            userService.saveUser(UserMapper.toDto(updatedUser));
        }

        verify(userDao, times(1)).update(any(User.class));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void getAllUsers() throws SQLException {
        List<User> expected = List.of(
                getUser(FIRST_USER_NAME, FIRST_USER_EMAIL),
                getUser(SECOND_USER_NAME, SECOND_USER_EMAIL));

        when(userDao.findAll()).thenReturn(expected);

        List<UserDto> actual = userService.getAllUsers();

        assertNotNull(actual);
        assertThat(actual, hasSize(expected.size()));
        verify(userDao, times(1)).findAll();
    }

    @Test
    void deleteUser_returnDeletedUserId() throws SQLException {
        User user = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
        user.setId(FIRST_ID);
        try (MockedStatic<GetProvider> getProvider = Mockito.mockStatic(GetProvider.class)) {
            getProvider.when(() -> GetProvider.getEntityId(anyString())).thenReturn(FIRST_ID);
            getProvider.when(() -> GetProvider.getUser(FIRST_ID)).thenReturn(user);

            doNothing().when(userDao).deleteById(FIRST_ID);
            Long id = userService.deleteUser("path");

            assertThat(id, equalTo(FIRST_ID));
            verify(userDao, times(1)).deleteById(FIRST_ID);
        }
    }
}
package com.aston.restservice.service.impl;

import com.aston.restservice.dto.UserDto;
import com.aston.restservice.mapper.UserMapper;
import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.UserDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.FIRST_USER_EMAIL;
import static com.aston.restservice.testData.TestConstants.FIRST_USER_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDaoImpl userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUser() throws SQLException {
        User expected = getUser(FIRST_USER_NAME, FIRST_USER_EMAIL);
        when(userDao.save(any(User.class))).thenReturn(Optional.of(expected));

        UserDto actual = userService.saveUser(UserMapper.toDto(expected));

        assertThat(expected.getName(), equalTo(actual.getName()));
        assertThat(expected.getEmail(), equalTo(actual.getEmail()));
        assertNotNull(actual.getId());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void deleteUser() {
    }

    private User getUser(String username, String email) {
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        return user;
    }
}
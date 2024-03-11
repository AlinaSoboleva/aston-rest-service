package com.aston.restservice.repository;

import com.aston.restservice.model.User;
import com.aston.restservice.repository.impl.UserDaoImpl;
import com.aston.restservice.util.GetProvider;
import org.junit.jupiter.api.*;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.aston.restservice.testData.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class UserDaoImplTest extends AbstractBaseDaoTest {

    private UserDaoImpl userDao;

    private User firstUser;

    @BeforeEach
    void setUp() {
        userDao = (UserDaoImpl) GetProvider.getUserDao();
        userDao.setConnectionBuilder(getConnectionBuilder());
        firstUser = getFirstUser();
    }


    @Test
    public void saveUser_usualCase() throws SQLException {
        Optional<User> savedUserOpt = userDao.save(firstUser);

        assertThat(savedUserOpt.isPresent(), equalTo(true));
        User savedUser = savedUserOpt.get();

        assertThat(savedUser.getId(), equalTo(FIRST_ID));
        assertThat(savedUser.getName(), equalTo(FIRST_USER_NAME));
        assertThat(savedUser.getEmail(), equalTo(FIRST_USER_EMAIL));

        userDao.deleteById(savedUser.getId());
    }

    @Test
    public void saveUser_whenEmailExist_ThrowPSQLException() throws SQLException {
        Optional<User> savedUserOpt = userDao.save(firstUser);

        User secondUser = getSecondUser();
        secondUser.setEmail(FIRST_USER_EMAIL);

        Exception exception = assertThrows(SQLException.class, () ->
                userDao.save(secondUser));

        assertThat(exception.getClass(), equalTo(PSQLException.class));

        userDao.deleteById(savedUserOpt.get().getId());
    }

    @Test
    public void saveUser_whenNameIsNull_ThrowPSQLException() {
        firstUser.setName(null);

        Exception exception = assertThrows(SQLException.class, () ->
                userDao.save(firstUser));

        assertThat(exception.getClass(), equalTo(PSQLException.class));
    }

    @Test
    public void saveUser_whenEmailIsNull_ThrowPSQLException() {
        firstUser.setEmail(null);

        Exception exception = assertThrows(SQLException.class, () ->
                userDao.save(firstUser));

        assertThat(exception.getClass(), equalTo(PSQLException.class));
    }

    @Test
    public void findUserById_usualCase() throws SQLException {
        Optional<User> savedUserOpt = userDao.save(firstUser);

        Optional<User> resultOpt = userDao.findById(savedUserOpt.get().getId());

        assertThat(resultOpt.isPresent(), equalTo(true));
        assertThat(resultOpt.get().getName(), equalTo(FIRST_USER_NAME));
        assertThat(resultOpt.get().getEmail(), equalTo(FIRST_USER_EMAIL));

        userDao.deleteById(savedUserOpt.get().getId());
    }

    @Test
    public void findUserById_whenIdNotFound_returnNull() throws SQLException {
        Optional<User> resultOpt = userDao.findById(FIRST_ID);

        assertFalse(resultOpt.isPresent());
    }

    @Test
    public void findAllUsers_usualCase_returnUsersList() throws SQLException {
        Optional<User> savedUserOpt = userDao.save(firstUser);

        User secondUser = getSecondUser();
        Optional<User> savedUserOpt2 = userDao.save(secondUser);

        List<User> result = userDao.findAll();

        userDao.deleteById(savedUserOpt.get().getId());
        userDao.deleteById(savedUserOpt2.get().getId());

        assertThat(result, hasSize(2));
    }

    @Test
    public void updateUserName_usualCase() throws SQLException {
        User savedUser = userDao.save(firstUser).get();
        savedUser.setName(UPDATED_USER_NAME);

        userDao.update(savedUser);
        Optional<User> updatedUser  = userDao.findById(savedUser.getId());
        userDao.deleteById(savedUser.getId());

        assertThat(updatedUser.isPresent(), equalTo(true));
        assertThat(updatedUser.get().getName(), equalTo(UPDATED_USER_NAME));
        assertThat(updatedUser.get().getEmail(), equalTo(FIRST_USER_EMAIL));
    }

    @Test
    public void updateUserEmail_usualCase() throws SQLException {
        User savedUser = userDao.save(firstUser).get();
        savedUser.setEmail(UPDATED_USER_EMAIL);

        userDao.update(savedUser);
        Optional<User> updatedUser  = userDao.findById(savedUser.getId());
        userDao.deleteById(savedUser.getId());

        assertThat(updatedUser.isPresent(), equalTo(true));
        assertThat(updatedUser.get().getName(), equalTo(FIRST_USER_NAME));
        assertThat(updatedUser.get().getEmail(), equalTo(UPDATED_USER_EMAIL));
    }
}

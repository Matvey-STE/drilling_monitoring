package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserDaoTest {
    private final UserDao userDao = UserDao.getInstance();
    private User saved;


    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();
    }

    @AfterEach
    void tearDown() {
        try {
            userDao.delete(saved.getUserId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static User getUserObject() {
        return User.builder()
                .userName("Test")
                .email("Test")
                .password("Test")
                .role(Role.USER)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .firstName("Test")
                .lastName("Test")
                .build();
    }

    @Test
    void save() {
        User test = getUserObject();

        saved = userDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getUserName(), saved.getUserName());
        assertEquals(test.getCreatedAt(), saved.getCreatedAt());
        assertEquals(test.getLastName(), saved.getLastName());
    }

    @Test
    void findAll() {
        User test = getUserObject();
        saved = userDao.save(test);

        List<User> list = userDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        User test = getUserObject();
        saved = userDao.save(test);
        Optional<User> optional = userDao.findById(saved.getUserId());
        assertTrue(optional.isPresent());
        User find = optional.get();
        assertEquals(saved.getUserId(), find.getUserId());
        assertEquals(test.getRole(), find.getRole());
        assertEquals(test.getPassword(), find.getPassword());
    }

    @Test
    void update() {
        User test = getUserObject();
        saved = userDao.save(test);
        User updatedObject = new User(saved.getUserId(), "updatedTest", "updatedTest", "updatedTestupdatedTest", Role.ADMIN, Timestamp.valueOf(LocalDateTime.now()),
                "updatedTest", "updatedTest");
        boolean updated = userDao.update(updatedObject);
        assertTrue(updated);
        Optional<User> find = userDao.findById(updatedObject.getUserId());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.getUserId(), find.get().getUserId());
        assertEquals(updatedObject.getRole(), find.get().getRole());
    }

    @Test
    void delete() {
        User test = getUserObject();
        saved = userDao.save(test);
        boolean deleted = userDao.delete(saved.getUserId());
        assertTrue(deleted);
    }

    @Test
    void findByFilter() {
        if (userDao.findById(1).isPresent()) {
            saved = userDao.findById(1).get();
        }

        UserDaoFilter userDaoFilter1 =
                new UserDaoFilter(saved.getUserName(), null, null);
        if (userDao.findByFilter(userDaoFilter1).isPresent()) {
            User byFilter1 = userDao.findByFilter(userDaoFilter1).get();
            assertEquals(saved.getUserId(), byFilter1.getUserId());
        }

        UserDaoFilter userDaoFilter2 =
                new UserDaoFilter(null, saved.getEmail(), null);
        if (userDao.findByFilter(userDaoFilter2).isPresent()) {
            User byFilter2 = userDao.findByFilter(userDaoFilter2).get();
            assertEquals(saved.getUserId(), byFilter2.getUserId());
        }

        UserDaoFilter userDaoFilter3 =
                new UserDaoFilter(null, null, saved.getPassword());
        if (userDao.findByFilter(userDaoFilter3).isPresent()) {
            User byFilter3 = userDao.findByFilter(userDaoFilter3).get();
            assertEquals(saved.getUserId(), byFilter3.getUserId());
        }


        UserDaoFilter userDaoFilter4 =
                new UserDaoFilter(saved.getUserName(), saved.getEmail(), saved.getPassword());
        if (userDao.findByFilter(userDaoFilter4).isPresent()) {
            User byFilter4 = userDao.findByFilter(userDaoFilter4).get();
            assertEquals(saved.getUserId(), byFilter4.getUserId());
        }
    }
}

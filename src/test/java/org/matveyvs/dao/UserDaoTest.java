package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.entity.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserDaoTest {
    private SessionFactory sessionFactory;
    private UserDao userDao = UserDao.getInstance();
    private User saved;
    private Integer userDbSize;

    private String getResetUserIdSql() {
        return "ALTER SEQUENCE drilling.public.users_user_id_seq RESTART WITH " + userDbSize;
    }

    @BeforeEach
    void setUp() {
        userDbSize = userDao.findAll().size() + 1;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            userDao.delete(saved.getUserId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        if (sessionFactory != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                NativeQuery<?> nativeQuery2 = session.createNativeQuery(getResetUserIdSql());
                nativeQuery2.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                log.info("Information: " + e);
            } finally {
                sessionFactory.close();
            }
        }
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
        saved = userDao.save(getUserObject());
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
        User test = getUserObject();
        saved = userDao.save(test);

        UserDaoFilter userDaoFilter1 =
                new UserDaoFilter(saved.getUserName(), null,null);
        Optional<User> byFilter1 = userDao.findByFilter(userDaoFilter1);
        assertEquals(saved.getUserId(), byFilter1.get().getUserId());

        UserDaoFilter userDaoFilter2 =
                new UserDaoFilter(null, saved.getEmail(),null);
        Optional<User> byFilter2 = userDao.findByFilter(userDaoFilter2);
        assertEquals(saved.getUserId(), byFilter2.get().getUserId());

        UserDaoFilter userDaoFilter3 =
                new UserDaoFilter(null, null,saved.getPassword());
        Optional<User> byFilter3 = userDao.findByFilter(userDaoFilter3);
        assertEquals(saved.getUserId(), byFilter3.get().getUserId());


        UserDaoFilter userDaoFilter4 =
                new UserDaoFilter(saved.getUserName(), saved.getEmail(),saved.getPassword());
        Optional<User> byFilter4 = userDao.findByFilter(userDaoFilter4);
        assertEquals(saved.getUserId(), byFilter4.get().getUserId());
    }
}

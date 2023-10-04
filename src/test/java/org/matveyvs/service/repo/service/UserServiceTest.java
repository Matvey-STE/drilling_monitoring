package org.matveyvs.service.repo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.dao.repository.UserRepository;
import org.matveyvs.dto.repo.UserCreateDto;
import org.matveyvs.dto.repo.UserReadDto;
import org.matveyvs.entity.Role;
import org.matveyvs.mapper.repo.UserMapperImpl;
import org.matveyvs.utils.HibernateUtil;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceTest {
    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private UserService userService;
    private Session session;

    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();

        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, arg1) -> method.invoke(sessionFactory.getCurrentSession(), arg1));
        session = sessionFactory.openSession();
        session.beginTransaction();
        userService = new UserService(new UserRepository(session), new UserMapperImpl());
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();

        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static UserCreateDto getUserObject() {
        return new UserCreateDto(
                "Test",
                "matvey@mail.ru",
                "Test",
                Role.USER,
                Timestamp.valueOf(LocalDateTime.now()),
                "Test",
                "Test"
        );
    }

    private static UserReadDto getUserUpdateObject() {
        return new UserReadDto(1,
                "Update",
                "matvei@email.com",
                "Update",
                Role.USER,
                Timestamp.valueOf(LocalDateTime.now()),
                "Update",
                "Update"
        );
    }

    @Test
    void create() {
        UserCreateDto object = getUserObject();
        Integer byId = userService.create(object);
        Optional<UserReadDto> saved = userService.findById(byId);
        assertTrue(saved.isPresent());
        assertEquals(object.createdAt(), saved.get().createdAt());
    }

    @Test
    void update() {
        Optional<UserReadDto> saved = userService.findById(1);
        UserReadDto objectToUpdate = getUserUpdateObject();
        boolean update = userService.update(objectToUpdate);
        assertTrue(update);
        Optional<UserReadDto> updated = userService.findById(1);
        assertNotEquals(saved.get().userName(), updated.get().userName());
        assertNotEquals(saved.get().password(), updated.get().password());
        assertEquals(objectToUpdate.createdAt(), updated.get().createdAt());
    }

    @Test
    void findById() {
        UserCreateDto object = getUserObject();
        Integer id = userService.create(object);
        Optional<UserReadDto> byId = userService.findById(id);
        assertEquals(object.email(), byId.get().email());
    }

    @Test
    void findAll() {
        UserCreateDto object = getUserObject();
        userService.create(object);
        List<UserReadDto> all = userService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void delete() {
        UserCreateDto object = getUserObject();
        Integer id = userService.create(object);
        boolean delete = userService.delete(id);
        assertTrue(delete);
        Optional<UserReadDto> byId = userService.findById(id);
        assertTrue(byId.isEmpty());
    }

    @Test
    void findByFilter() {
        UserCreateDto object = getUserObject();
        Integer id = userService.create(object);
        Optional<UserReadDto> saved = userService.findById(id);

        UserDaoFilter userDaoFilter1 =
                new UserDaoFilter(saved.get().userName(), null, null);
        if (userService.findByFilter(userDaoFilter1).isPresent()) {
            UserReadDto byFilter1 = userService.findByFilter(userDaoFilter1).get();
            assertEquals(saved.get().userName(), byFilter1.userName());
        }

        UserDaoFilter userDaoFilter2 =
                new UserDaoFilter(null, saved.get().email(), null);
        if (userService.findByFilter(userDaoFilter2).isPresent()) {
            UserReadDto byFilter2 = userService.findByFilter(userDaoFilter2).get();
            assertEquals(saved.get().userName(), byFilter2.userName());
        }

        UserDaoFilter userDaoFilter3 =
                new UserDaoFilter(null, null, saved.get().password());
        if (userService.findByFilter(userDaoFilter3).isPresent()) {
            UserReadDto byFilter3 = userService.findByFilter(userDaoFilter3).get();
            assertEquals(saved.get().firstName(), byFilter3.firstName());
        }


        UserDaoFilter userDaoFilter4 =
                new UserDaoFilter(saved.get().userName(),
                        saved.get().email(), saved.get().password());
        if (userService.findByFilter(userDaoFilter4).isPresent()) {
            UserReadDto byFilter4 = userService.findByFilter(userDaoFilter4).get();
            assertEquals(saved.get().userName(), byFilter4.userName());
        }

    }

    @Test
    void login() {
        UserCreateDto object = getUserObject();
        Integer id = userService.create(object);
        Optional<UserReadDto> saved = userService.findById(id);
        Optional<UserReadDto> login = userService.login(saved.get().email(),
                saved.get().password());
        assertEquals(login.get().lastName(), saved.get().lastName());
    }

    @Test
    void checkIfExist() {
        UserCreateDto object = getUserObject();
        Integer id = userService.create(object);
        Optional<UserReadDto> saved = userService.findById(id);
        boolean b = userService.checkIfExist(saved.get().userName(), saved.get().email());
        assertTrue(b);
    }

    private static UserCreateDto getConstraintUser() {
        return new UserCreateDto(
                null,
                "Update",
                null,
                Role.USER,
                null,
                "Update",
                "Update"
        );
    }

    @Test
    void constraintExpectation() {
        UserCreateDto object = getConstraintUser();
        assertThrows(ConstraintViolationException.class,
                () ->  userService.create(object));
    }
}
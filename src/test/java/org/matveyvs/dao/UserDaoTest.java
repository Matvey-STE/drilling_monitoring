package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.*;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private static WellData wellData;
    private static long newIdPointToReset;
    private static long newWellDataIdPointToReset;
    private long testKey;
    private UserDao userDao;
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ?
            """;
    private static final String DELETE_DIR_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.users_user_id_seq RESTART WITH " + newIdPointToReset;
    }

    private String getWellResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + newWellDataIdPointToReset;
    }


    @BeforeEach
    void setUp() {
        userDao = UserDao.getInstance();
//        wellDataDao = WellDataDao.getInstance();

        wellData = wellDataDao.save(getWellObject());
        connection = ConnectionManager.open();

        newIdPointToReset = userDao.findAll().size() + 1;
        newWellDataIdPointToReset = wellDataDao.findAll().size() + 1;
    }

    @AfterEach
    void tearDown() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setLong(1, testKey);
        statement.executeUpdate();

        PreparedStatement statementWell = connection.prepareStatement(DELETE_DIR_SQL);
        statementWell.setLong(1, wellData.id());
        statementWell.executeUpdate();

        Statement resetStatementId = connection.createStatement();
        resetStatementId.execute(getResetIdTableSql());

        Statement resetWellStatementId = connection.createStatement();
        resetWellStatementId.execute(getWellResetIdTableSql());

        connection.close();
    }

    private static User getObject() {
//        wellData = getWellObject();
        return new User("Test", "Test", "Test", Status.USER, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), "Test", "Test", wellData);
    }

    private static WellData getWellObject() {
        return new WellData("Test", "Test", "Test",
                "Test");
    }

    @Test
    void save() {
        User test = getObject();

        User saved = userDao.save(test);

        assertNotNull(saved);
        assertEquals(test.userName(), saved.userName());
        assertEquals(test.createdAt(), saved.createdAt());
        assertEquals(test.lastName(), saved.lastName());

        testKey = saved.id();
    }

    @Test
    void findAll() {
        User test = getObject();

        User saved = userDao.save(test);

        List<User> list = userDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
        testKey = saved.id();
    }

    @Test
    void findById() {
        User test = getObject();

        User saved = userDao.save(test);

        Optional<User> optional = userDao.findById(saved.id());

        assertTrue(optional.isPresent());
        User find = optional.get();
        assertEquals(saved.id(), find.id());
        assertEquals(test.lastName(), find.lastName());
        assertEquals(test.password(), find.password());
        testKey = saved.id();
    }

    @Test
    void update() {
        User saved = userDao.save(getObject());

        User updatedObject = new User(saved.id(), "updatedTest", "updatedTest", "updatedTestupdatedTest", Status.ADMIN, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), "updatedTest", "updatedTest", wellData);
        boolean updated = userDao.update(updatedObject);

        assertTrue(updated);
        Optional<User> find = userDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.id(), find.get().id());
        assertEquals(updatedObject.status(), find.get().status());
        testKey = updatedObject.id();
    }

    @Test
    void delete() {
        User test = getObject();

        User saved = userDao.save(test);

        boolean deleted = userDao.delete(saved.id());
        assertTrue(deleted);
        Optional<User> deletedObject = userDao.findById(testKey);
        assertTrue(deletedObject.isEmpty());

        testKey = saved.id();
    }
}

package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.*;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WellDataDaoTest {
    private static long newIdPointToReset;
    private long testKey;
    private WellDataDao wellDataDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        wellDataDao = WellDataDao.getInstance();

        connection = ConnectionManager.open();

        newIdPointToReset = wellDataDao.findAll().size() + 1;
    }

    @AfterEach
    void tearDown() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setLong(1, testKey);
        statement.executeUpdate();

        Statement resetStatementId = connection.createStatement();
        resetStatementId.execute(getResetIdTableSql());

        connection.close();
    }

    private static WellData getObject() {
        return new WellData("Test", "Test", "Test",
                "Test");
    }

    @Test
    void save() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        assertNotNull(saved);
        assertEquals(test.companyName(), saved.companyName());
        assertEquals(test.fieldName(), saved.fieldName());

        testKey = saved.id();
    }

    @Test
    void findAll() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        List<WellData> list = wellDataDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
        testKey = saved.id();
    }

    @Test
    void findById() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        Optional<WellData> optional = wellDataDao.findById(saved.id());

        assertTrue(optional.isPresent());
        WellData find = optional.get();
        assertEquals(saved.id(), find.id());
        assertEquals(test.companyName(), find.companyName());
        assertEquals(test.well(), find.well());
        testKey = saved.id();
    }

    @Test
    void update() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        WellData updatedObject = new WellData(saved.id(), "updatedTest", "updatedTest", "updatedTest",
                "updatedTest");

        boolean updated = wellDataDao.update(updatedObject);

        assertTrue(updated);

        Optional<WellData> find = wellDataDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.companyName(), find.get().companyName());
        testKey = updatedObject.id();
    }

    @Test
    void delete() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        boolean deleted = wellDataDao.delete(saved.id());
        assertTrue(deleted);
        Optional<WellData> deletedObject = wellDataDao.findById(testKey);
        assertTrue(deletedObject.isEmpty());

        testKey = saved.id();
    }
}
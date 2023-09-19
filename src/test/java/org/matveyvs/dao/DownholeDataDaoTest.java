package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DownholeDataDaoTest {
    private static WellData wellData;
    private static long newIdPointToReset;
    private long testKey;
    private DownholeDataDao downholeDataDao;
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM downhole_data
            WHERE id = ?
            """;
    private static final String DELETE_WELLDATA_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.downhole_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        downholeDataDao = DownholeDataDao.getInstance();

        wellData = wellDataDao.save(getWellObject());

        connection = ConnectionManager.open();

        newIdPointToReset = downholeDataDao.findAll().size() + 1;
    }

    @AfterEach
    void tearDown() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setLong(1, testKey);
        statement.executeUpdate();


        PreparedStatement statement2 = connection.prepareStatement(DELETE_WELLDATA_SQL);
        statement2.setLong(1, wellData.id());
        statement2.executeUpdate();


        Statement resetStatementId = connection.createStatement();
        resetStatementId.execute(getResetIdTableSql());

        Statement resetStatementId2 = connection.createStatement();
        resetStatementId2.execute(getWellIdTableSql());

        connection.close();
    }

    private static DownholeData getObject() {
        return new DownholeData(wellData);
    }

    private static WellData getWellObject() {
        return new WellData("Test", "Test", "Test",
                "Test");
    }

    @Test
    void save() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        assertNotNull(saved);
        assertEquals(test.wellData(), saved.wellData());

        testKey = saved.id();
    }

    @Test
    void findAll() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        List<DownholeData> list = downholeDataDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
        testKey = saved.id();
    }

    @Test
    void findById() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        Optional<DownholeData> optional = downholeDataDao.findById(saved.id());

        assertTrue(optional.isPresent());
        DownholeData find = optional.get();
        assertEquals(saved.id(), find.id());
        assertEquals(test.wellData(), find.wellData());

        testKey = saved.id();
    }

    @Test
    void update() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        DownholeData updatedObject = new DownholeData(saved.id(),
                wellData);

        boolean updated = downholeDataDao.update(updatedObject);

        assertTrue(updated);

        Optional<DownholeData> find = downholeDataDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.wellData(), find.get().wellData());
        testKey = updatedObject.id();
    }

    @Test
    void delete() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        boolean deleted = downholeDataDao.delete(saved.id());
        assertTrue(deleted);
        Optional<DownholeData> deletedAirport = downholeDataDao.findById(testKey);
        assertTrue(deletedAirport.isEmpty());

        testKey = saved.id();
    }
}

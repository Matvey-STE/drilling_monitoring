package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Directional;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.Gamma;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DownholeDataDaoTest {
    private static Directional directionalTest;
    private static Gamma gammaTest;
    private static long newIdPointToReset;
    private long testKey;
    private DownholeDataDao downholeDataDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM downhole_data
            WHERE id = ?
            """;


    private String getResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.downhole_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        downholeDataDao = DownholeDataDao.getInstance();


        directionalTest = getDirectionalObject();
        gammaTest = getGammaObject();
        connection = ConnectionManager.open();

        newIdPointToReset = downholeDataDao.findAll().size() + 1;
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

    private static DownholeData getObject() {
        return new DownholeData(Timestamp.valueOf(LocalDateTime.now()),
                directionalTest, gammaTest);
    }

    private static Gamma getGammaObject() {
        return new Gamma(1L,22.22, 22.22);
    }

    private static Directional getDirectionalObject() {
        return new Directional(1L,22.22, 22.22, 22.22, 22.22,
                22.22, 22.22, 22.22, 22.22, 22.22,
                22.22, 22.22, 22.22);
    }

    @Test
    void save() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        assertNotNull(saved);
        assertEquals(test.measureDate(), saved.measureDate());
        assertEquals(test.directional(), saved.directional());
        assertEquals(test.gamma(), saved.gamma());

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
        assertEquals(test.measureDate(), find.measureDate());

        testKey = saved.id();
    }

    @Test
    void update() {
        DownholeData test = getObject();

        DownholeData saved = downholeDataDao.save(test);

        DownholeData updatedObject = new DownholeData(saved.id(), Timestamp.valueOf(LocalDateTime.now()),
                directionalTest, gammaTest);

        boolean updated = downholeDataDao.update(updatedObject);

        assertTrue(updated);

        Optional<DownholeData> find = downholeDataDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.measureDate(), find.get().measureDate());
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
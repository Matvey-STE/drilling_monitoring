package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.entity.WellData;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

class SurfaceDataDaoTest {
    private static long newIdPointToReset;
    private long testKey;
    private SurfaceDataDao surfaceDataDao;
    private static WellData wellData;
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM surface_data
            WHERE id = ?
            """;
    private static final String DELETE_WELLDATA_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.surface_data_id_seq RESTART WITH " + newIdPointToReset;
    }
    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        surfaceDataDao = SurfaceDataDao.getInstance();
        connection = ConnectionManager.open();

        wellData = wellDataDao.save(getWellObject());

        newIdPointToReset = surfaceDataDao.findAll().size() + 1;
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
    private static SurfaceData getObject() {
        return new SurfaceData(Timestamp.valueOf(LocalDateTime.now()),
                            22.22, 22.22,22.22,
                        22.22,22.22, 22.22,22.22, wellData);
    }
    private static WellData getWellObject() {
        return new WellData("Test", "Test", "Test",
                "Test");
    }

    @Test
    void save() {
        SurfaceData test = getObject();

        SurfaceData saved = surfaceDataDao.save(test);

        assertNotNull(saved);
        assertEquals(test.measuredDate(), saved.measuredDate());
        assertEquals(test.measuredDepth(), saved.measuredDepth());

        testKey = saved.id();
    }

    @Test
    void findAll() {
        SurfaceData test = getObject();

        SurfaceData saved = surfaceDataDao.save(test);

        List<SurfaceData> list = surfaceDataDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
        testKey = saved.id();
    }

    @Test
    void findById() {
        SurfaceData test = getObject();

        SurfaceData saved = surfaceDataDao.save(test);

        Optional<SurfaceData> optional = surfaceDataDao.findById(saved.id());

        assertTrue(optional.isPresent());
        SurfaceData find = optional.get();
        assertEquals(saved.id(), find.id());
        assertEquals(test.measuredDepth(), find.measuredDepth());
        assertEquals(test.measuredDate(), find.measuredDate());
        testKey = saved.id();
    }

    @Test
    void update() {
        SurfaceData test = getObject();

        SurfaceData saved = surfaceDataDao.save(test);

        SurfaceData updatedObject = new SurfaceData(saved.id(),Timestamp.valueOf(LocalDateTime.now()),
                33.33, 22.22,22.22,
                22.22,22.22, 33.33,33.33, wellData);

        boolean updated = surfaceDataDao.update(updatedObject);

        assertTrue(updated);

        Optional<SurfaceData> find = surfaceDataDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.measuredDepth(), find.get().measuredDepth());
        assertEquals(updatedObject.measuredDate(), find.get().measuredDate());
        assertEquals(updatedObject.blockPos(), find.get().blockPos());

        testKey = updatedObject.id();
    }

    @Test
    void delete() {
        SurfaceData test = getObject();

        SurfaceData saved = surfaceDataDao.save(test);

        boolean deleted = surfaceDataDao.delete(saved.id());
        assertTrue(deleted);
        Optional<SurfaceData> deletedAirport = surfaceDataDao.findById(testKey);
        assertTrue(deletedAirport.isEmpty());

        testKey = saved.id();
    }
}

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

class WellDataDaoTest {
    private static SurfaceData surfaceData;
    private static DownholeData downholeData;
    private static long newIdPointToReset;
    private static long newSurfaceDataIdPointToReset;
    private static long newDownholeIdPointToReset;
    private long testKey;
    private WellDataDao wellDataDao;
    private SurfaceDataDao surfaceDataDao;
    private DownholeDataDao downholeDataDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;
    private static final String DELETE_DIR_SQL = """
            DELETE FROM surface_data
            WHERE id = ?
            """;
    private static final String DELETE_GAM_SQL = """
            DELETE FROM downhole_data
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.well_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    private String getSurfaceResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.surface_data_id_seq RESTART WITH " + newSurfaceDataIdPointToReset;
    }

    private String getDownholeResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.downhole_data_id_seq RESTART WITH " + newDownholeIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        wellDataDao = WellDataDao.getInstance();
        surfaceDataDao = SurfaceDataDao.getInstance();
        downholeDataDao = DownholeDataDao.getInstance();

        surfaceData = surfaceDataDao.save(getSufaceDataObject());
        downholeData = downholeDataDao.save(getDownholeDataObject());
        connection = ConnectionManager.open();

        newIdPointToReset = downholeDataDao.findAll().size() + 1;
        newSurfaceDataIdPointToReset = surfaceDataDao.findAll().size() + 1;
        newDownholeIdPointToReset = downholeDataDao.findAll().size() + 1;
    }

    @AfterEach
    void tearDown() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setLong(1, testKey);
        statement.executeUpdate();

        PreparedStatement statementGam = connection.prepareStatement(DELETE_DIR_SQL);
        statementGam.setLong(1, downholeData.id());
        statementGam.executeUpdate();

        PreparedStatement statementDir = connection.prepareStatement(DELETE_GAM_SQL);
        statementDir.setLong(1, surfaceData.id());
        statementDir.executeUpdate();

        Statement resetStatementId = connection.createStatement();
        resetStatementId.execute(getResetIdTableSql());

        Statement resetSurfaceStatementId = connection.createStatement();
        resetSurfaceStatementId.execute(getSurfaceResetIdTableSql());

        Statement resetDownholeStatementId = connection.createStatement();
        resetDownholeStatementId.execute(getDownholeResetIdTableSql());

        connection.close();
    }

    private static WellData getObject() {
        return new WellData("Test", "Test", "Test",
                "Test", surfaceData, downholeData);
    }

    private static DownholeData getDownholeDataObject() {
        return new DownholeData(Timestamp.valueOf(LocalDateTime.now()),
                new Directional(1L, 22.22, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22), new Gamma(1L, 22.22, 22.22)
        );
    }

    private static SurfaceData getSufaceDataObject() {
        return new SurfaceData(Timestamp.valueOf(LocalDateTime.now()),
                22.22, 22.22, 22.22,
                22.22, 22.22, 22.22, 22.22);
    }

    @Test
    void save() {
        WellData test = getObject();

        WellData saved = wellDataDao.save(test);

        assertNotNull(saved);
        assertEquals(test.companyName(), saved.companyName());
        assertEquals(test.fieldName(), saved.fieldName());
        assertEquals(test.surfaceData(), saved.surfaceData());

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
                "updatedTest", surfaceData, downholeData);

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
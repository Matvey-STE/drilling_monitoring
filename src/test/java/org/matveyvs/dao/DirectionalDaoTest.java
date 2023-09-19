package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Directional;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DirectionalDaoTest {
    private static WellData wellData;
    private static DownholeData downholeData;
    private static long newIdPointToReset;
    private long testKey;
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private DirectionalDao directionalDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM directional
            WHERE id = ?
            """;
    private static final String DELETE_DOWNHOLE_SQL = """
            DELETE FROM downhole_data
            WHERE id = ?
            """;
    private static final String DELETE_WELLDATA_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.directional_new_id_seq RESTART WITH " + newIdPointToReset;
    }

    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    private String getDownholeIdTableSql() {
        return "ALTER SEQUENCE drilling.public.downhole_data_id_seq RESTART WITH " + newIdPointToReset;
    }

    private static DownholeData getDownholeObject() {
        return new DownholeData(wellData);
    }

    private static WellData getWellObject() {
        return new WellData("Test", "Test", "Test",
                "Test");
    }

    @BeforeEach
    void setUp() {
        directionalDao = DirectionalDao.getInstance();
        connection = ConnectionManager.open();

        wellData = wellDataDao.save(getWellObject());
        downholeData = downholeDataDao.save(getDownholeObject());

        newIdPointToReset = directionalDao.findAll().size() + 1;
    }

    @AfterEach
    void tearDown() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setLong(1, testKey);
        statement.executeUpdate();

        PreparedStatement statement2 = connection.prepareStatement(DELETE_DOWNHOLE_SQL);
        statement2.setLong(1, downholeData.id());
        statement2.executeUpdate();

        PreparedStatement statement3 = connection.prepareStatement(DELETE_WELLDATA_SQL);
        statement3.setLong(1, wellData.id());
        statement3.executeUpdate();

        Statement resetStatementId = connection.createStatement();
        resetStatementId.execute(getResetIdTableSql());

        Statement resetStatementId2 = connection.createStatement();
        resetStatementId2.execute(getWellIdTableSql());

        Statement resetStatementId3 = connection.createStatement();
        resetStatementId3.execute(getDownholeIdTableSql());
        connection.close();
    }

    private static Directional getDirectional() {
        return new Directional(Timestamp.valueOf(LocalDateTime.now()), 22.22, 22.22, 22.22, 22.22,
                22.22, 22.22, 22.22, 22.22, 22.22,
                22.22, 22.22, 22.22, downholeData);
    }

    @Test
    void save() {
        Directional testDirectional = getDirectional();

        Directional savedDirectional = directionalDao.save(testDirectional);

        assertNotNull(savedDirectional);
        assertEquals(testDirectional.measuredDepth(), savedDirectional.measuredDepth());
        assertEquals(testDirectional.measuredDepth(), savedDirectional.measuredDepth());
        assertEquals(testDirectional.azCorr(), savedDirectional.azCorr());
        assertEquals(testDirectional.toolfaceCorr(), savedDirectional.toolfaceCorr());

        testKey = savedDirectional.id();
    }

    @Test
    void findAll() {
        Directional testDirectional = getDirectional();

        Directional savedDirectional = directionalDao.save(testDirectional);

        List<Directional> directionals = directionalDao.findAll();

        assertNotNull(directionals);
        assertTrue(directionals.size() > 0);
        testKey = savedDirectional.id();
    }

    @Test
    void findById() {
        Directional testDirectional = getDirectional();

        Directional savedDirectional = directionalDao.save(testDirectional);

        Optional<Directional> optionalDirectional = directionalDao.findById(savedDirectional.id());

        assertTrue(optionalDirectional.isPresent());
        Directional directionalFind = optionalDirectional.get();
        assertEquals(savedDirectional.id(), directionalFind.id());
        assertEquals(savedDirectional.measuredDepth(), directionalFind.measuredDepth());
        assertEquals(savedDirectional.azCorr(), directionalFind.azCorr());
        testKey = savedDirectional.id();
    }

    @Test
    void update() {
        Directional testDirectional = getDirectional();

        Directional savedDirectional = directionalDao.save(testDirectional);
        Directional updatedDirectional =
                new Directional(savedDirectional.id(), Timestamp.valueOf(LocalDateTime.now()), 33.33, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22, 22.22, 22.22,
                        22.22, 33.33, 33.33, downholeData);


        boolean updatedResult = directionalDao.update(updatedDirectional);

        assertTrue(updatedResult);

        Optional<Directional> find = directionalDao.findById(updatedDirectional.id());
        assertTrue(find.isPresent());
        assertEquals(updatedDirectional.measuredDepth(), find.get().measuredDepth());
        assertEquals(updatedDirectional.azCorr(), find.get().azCorr());

        testKey = updatedDirectional.id();
    }

    @Test
    void delete() {
        Directional testDirectional = getDirectional();

        Directional savedDirectional = directionalDao.save(testDirectional);

        boolean deleted = directionalDao.delete(savedDirectional.id());
        assertTrue(deleted);
        // Verify that the airport has been deleted
        Optional<Directional> deletedAirport = directionalDao.findById(savedDirectional.id());
        assertTrue(deletedAirport.isEmpty());

        testKey = savedDirectional.id();
    }
}

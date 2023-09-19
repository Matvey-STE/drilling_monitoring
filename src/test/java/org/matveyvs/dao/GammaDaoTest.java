package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.Gamma;
import org.matveyvs.entity.WellData;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GammaDaoTest {
    private static WellData wellData;
    private static DownholeData downholeData;
    private static long newIdPointToReset;
    private long testKey;
    private GammaDao gammaDao;
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM gamma
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
        return "ALTER SEQUENCE drilling.public.gamma_new_id_seq RESTART WITH " + newIdPointToReset;
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
        gammaDao = GammaDao.getInstance();
        connection = ConnectionManager.open();

        wellData = wellDataDao.save(getWellObject());
        downholeData = downholeDataDao.save(getDownholeObject());

        newIdPointToReset = gammaDao.findAll().size() + 1;
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

    private static Gamma getObject() {
        return new Gamma(Timestamp.valueOf(LocalDateTime.now()),22.22, 22.22,downholeData);
    }


    @Test
    void save() {
        Gamma testGamma = getObject();

        Gamma savedGamma = gammaDao.save(testGamma);

        assertNotNull(savedGamma);
        assertEquals(testGamma.measuredDepth(), savedGamma.measuredDepth());
        assertEquals(testGamma.grcx(), savedGamma.grcx());

        testKey = savedGamma.id();
    }

    @Test
    void findAll() {
        Gamma test = getObject();

        Gamma saved = gammaDao.save(test);

        List<Gamma> list = gammaDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
        testKey = saved.id();
    }

    @Test
    void findById() {
        Gamma test = getObject();

        Gamma saved = gammaDao.save(test);

        Optional<Gamma> optional = gammaDao.findById(saved.id());

        assertTrue(optional.isPresent());
        Gamma gammaFind = optional.get();
        assertEquals(saved.id(), gammaFind.id());
        assertEquals(test.measuredDepth(), gammaFind.measuredDepth());
        assertEquals(test.grcx(), gammaFind.grcx());
        testKey = saved.id();
    }

    @Test
    void update() {
        Gamma test = getObject();

        Gamma saved = gammaDao.save(test);

        Gamma updatedObject = new Gamma(saved.id(), Timestamp.valueOf(LocalDateTime.now()),
                33.33, 33.33, downholeData);

        boolean updated = gammaDao.update(updatedObject);

        assertTrue(updated);

        Optional<Gamma> find = gammaDao.findById(updatedObject.id());
        assertTrue(find.isPresent());
        assertEquals(updatedObject.measuredDepth(), find.get().measuredDepth());
        assertEquals(updatedObject.grcx(), find.get().grcx());

        testKey = updatedObject.id();
    }

    @Test
    void delete() {
        Gamma test = getObject();

        Gamma savedGamma = gammaDao.save(test);

        boolean deleted = gammaDao.delete(savedGamma.id());
        assertTrue(deleted);
        // Verify that the airport has been deleted
        Optional<Gamma> deletedAirport = gammaDao.findById(testKey);
        assertTrue(deletedAirport.isEmpty());

        testKey = savedGamma.id();
    }
}

package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Gamma;
import org.matveyvs.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GammaDaoTest {
    private static long newIdPointToReset;
    private long testKey;
    private GammaDao gammaDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM gamma
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.gamma_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        gammaDao = GammaDao.getInstance();
        connection = ConnectionManager.open();

        newIdPointToReset = gammaDao.findAll().size() + 1;
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

    private static Gamma getObject() {
        Gamma testGamma = new Gamma(22.22, 22.22);
        return testGamma;
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

        Gamma updatedObject = new Gamma(saved.id(), 33.33, 33.33);

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
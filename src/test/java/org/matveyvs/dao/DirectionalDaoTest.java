package org.matveyvs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Directional;
import org.matveyvs.entity.Gamma;
import org.matveyvs.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DirectionalDaoTest {
    private static long newIdPointToReset;
    private long testKey;
    private DirectionalDao directionalDao;
    private Connection connection;
    private static final String DELETE_SQL = """
            DELETE FROM directional
            WHERE id = ?
            """;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE postgres.public.directional_id_seq RESTART WITH " + newIdPointToReset;
    }

    @BeforeEach
    void setUp() {
        directionalDao = DirectionalDao.getInstance();
        connection = ConnectionManager.open();

        newIdPointToReset = directionalDao.findAll().size() + 1;
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

    private static Directional getDirectional() {
        Directional testDirectional =
                new Directional(22.22, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22);
        return testDirectional;
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
                new Directional(savedDirectional.id(), 33.33, 22.22, 22.22, 22.22,
                        22.22, 22.22, 22.22, 22.22, 22.22,
                        22.22, 33.33, 33.33);


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
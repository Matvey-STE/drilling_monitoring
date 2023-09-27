package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WellDataDaoTest {
    private SessionFactory sessionFactory;
    private WellDataDao wellDataDao = WellDataDao.getInstance();
    private WellData saved;
    private Integer welldataDbSize;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + welldataDbSize;
    }

    @BeforeEach
    void setUp() {
        welldataDbSize = wellDataDao.findAll().size() + 1;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try {
            wellDataDao.delete(saved.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        if (sessionFactory != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                NativeQuery<?> nativeQuery2 = session.createNativeQuery(getResetIdTableSql());
                nativeQuery2.executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                log.info("Information: " + e);
            } finally {
                sessionFactory.close();
            }
        }
    }

    private static WellData getObject() {
        return WellData.builder()
                .companyName("Test")
                .fieldName("Test")
                .wellCluster("Test")
                .well("Test")
                .build();
    }

    @Test
    void save() {
        WellData test = getObject();

        saved = wellDataDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getCompanyName(), saved.getCompanyName());
        assertEquals(test.getWell(), saved.getWell());
    }

    @Test
    void findAll() {
        WellData test = getObject();
        saved = wellDataDao.save(test);

        List<WellData> list = wellDataDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        WellData test = getObject();
        saved = wellDataDao.save(getObject());
        Optional<WellData> optional = wellDataDao.findById(saved.getId());
        assertTrue(optional.isPresent());
        WellData find = optional.get();
        assertEquals(saved.getId(), find.getId());
        assertEquals(test.getWellCluster(), find.getWellCluster());
        assertEquals(test.getWell(), find.getWell());
    }

    @Test
    void update() {
        WellData test = getObject();
        saved = wellDataDao.save(test);
        String updateWell = "Update";
        saved.setWell(updateWell);
        boolean updated = wellDataDao.update(saved);
        assertTrue(updated);
        Optional<WellData> find = wellDataDao.findById(saved.getId());
        assertTrue(find.isPresent());
        assertEquals(updateWell, find.get().getWell());
    }

    @Test
    void delete() {
        WellData test = getObject();
        saved = wellDataDao.save(test);
        boolean deleted = wellDataDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

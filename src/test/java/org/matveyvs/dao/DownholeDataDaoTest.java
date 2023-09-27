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
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DownholeDataDaoTest {
    private SessionFactory sessionFactory;
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private DownholeData saved;
    private static WellData wellData;
    private Integer downholeDbSize;
    private Integer wellDataDbSize;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.downhole_data_id_seq RESTART WITH " + downholeDbSize;
    }

    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + wellDataDbSize;
    }

    @BeforeEach
    void setUp() {
        wellDataDbSize = wellDataDao.findAll().size() + 1;
        downholeDbSize = downholeDataDao.findAll().size() + 1;
        wellData = wellDataDao.save(getwelldataObject());
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            downholeDataDao.delete(saved.getId());
            wellDataDao.delete(wellData.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        if (sessionFactory != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                NativeQuery<?> nativeQuery1 = session.createNativeQuery(getResetIdTableSql());
                nativeQuery1.executeUpdate();
                NativeQuery<?> nativeQuery2 = session.createNativeQuery(getWellIdTableSql());
                nativeQuery2.executeUpdate();

                session.getTransaction().commit();
            } catch (Exception e) {
                log.info("Information: " + e);
            } finally {
                sessionFactory.close();
            }
        }
    }

    private static WellData getwelldataObject() {
        return WellData.builder()
                .companyName("Test")
                .fieldName("Test")
                .wellCluster("Test")
                .well("Test")
                .build();
    }

    private static DownholeData getObject() {
        return DownholeData.builder()
                .wellData(wellData)
                .build();
    }

    @Test
    void save() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getWellData(), saved.getWellData());
    }

    @Test
    void findAll() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        List<DownholeData> list = downholeDataDao.findAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        Optional<DownholeData> optional = downholeDataDao.findById(saved.getId());
        assertTrue(optional.isPresent());
        DownholeData find = optional.get();
        assertEquals(saved.getId(), find.getId());
        assertEquals(test.getWellData(), find.getWellData());
    }

    @Test
    void update() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        String updateWell = "update";
        wellData.setWell(updateWell);
        saved.setWellData(wellData);
        boolean updated = downholeDataDao.update(saved);
        assertTrue(updated);
        Optional<DownholeData> find = downholeDataDao.findById(saved.getId());
        assertTrue(find.isPresent());
        assertEquals(updateWell, find.get().getWellData().getWell());
    }

    @Test
    void delete() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        boolean deleted = downholeDataDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

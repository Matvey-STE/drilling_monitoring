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
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.entity.WellData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class SurfaceDataDaoTest {
    private SessionFactory sessionFactory;
    private final SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private SurfaceData saved;
    private static WellData wellData;
    private Integer surfaceDbSize;
    private Integer wellDataDbSize;

    private String getResetIdTableSql() {
        return "ALTER SEQUENCE drilling.public.surface_data_id_seq RESTART WITH " + surfaceDbSize;
    }
    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + wellDataDbSize;
    }

    @BeforeEach
    void setUp() {
        wellDataDbSize = wellDataDao.findAll().size() + 1;
        surfaceDbSize = surfaceDataDao.findAll().size() + 1;
        wellData = wellDataDao.save(getwelldataObject());
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
    void tearDown() {
        try {
            surfaceDataDao.delete(saved.getId());
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
    private static SurfaceData getObject() {
        return SurfaceData.builder()
                .measuredDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(21.25)
                .holeDepth(11.22)
                .tvDepth(21.22)
                .hookload(24.22)
                .wob(25.22)
                .blockPos(21.25)
                .standpipePressure(22.25)
                .wellData(wellData)
                .build();
    }

    @Test
    void save() {
        SurfaceData test = getObject();

        saved = surfaceDataDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getBlockPos(), saved.getBlockPos());
        assertEquals(test.getWob(), saved.getWob());
    }

    @Test
    void findAll() {
        SurfaceData test = getObject();
        saved = surfaceDataDao.save(test);

        List<SurfaceData> list = surfaceDataDao.findAll();

        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        SurfaceData test = getObject();
        saved = surfaceDataDao.save(test);

        Optional<SurfaceData> optional = surfaceDataDao.findById(saved.getId());
        assertTrue(optional.isPresent());
        SurfaceData find = optional.get();
        assertEquals(saved.getId(), find.getId());
        assertEquals(test.getWob(), find.getWob());
        assertEquals(test.getHoleDepth(), find.getHoleDepth());
    }

    @Test
    void update() {
        SurfaceData test = getObject();
        saved = surfaceDataDao.save(test);
        Double updateBlockPos = 999.99;
        saved.setBlockPos(updateBlockPos);
        boolean updated = surfaceDataDao.update(saved);
        assertTrue(updated);
        Optional<SurfaceData> find = surfaceDataDao.findById(saved.getId());
        assertTrue(find.isPresent());
        assertEquals(updateBlockPos, find.get().getBlockPos());
    }

    @Test
    void delete() {
        SurfaceData test = getObject();
        saved = surfaceDataDao.save(test);
        boolean deleted = surfaceDataDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

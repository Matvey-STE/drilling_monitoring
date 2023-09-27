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
import org.matveyvs.entity.Directional;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class DirectionalDaoTest {
    private SessionFactory sessionFactory;
    private final DirectionalDao directionalDao = DirectionalDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Directional saved;
    private static DownholeData downholeData;
    private static WellData wellData;
    private Integer downholeDbSize;
    private Integer wellDataDbSize;
    private Integer directionalDbSize;

    private String getResetDownholeIdTableSql() {
        return "ALTER SEQUENCE drilling.public.downhole_data_id_seq RESTART WITH " + downholeDbSize;
    }

    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + wellDataDbSize;
    }

    private String getResetGammaIdTableSql() {
        return "ALTER SEQUENCE drilling.public.directional_new_id_seq RESTART WITH " + directionalDbSize;
    }

    @BeforeEach
    void setUp() {
        directionalDbSize = directionalDao.findAll().size() + 1;
        wellDataDbSize = wellDataDao.findAll().size() + 1;
        downholeDbSize = downholeDataDao.findAll().size() + 1;

        wellData = wellDataDao.save(getwelldataObject());
        downholeData = downholeDataDao.save(getDownholeData());
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
            directionalDao.delete(saved.getId());
            downholeDataDao.delete(downholeData.getId());
            wellDataDao.delete(wellData.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        if (sessionFactory != null) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                NativeQuery<?> nativeQuery1 = session
                        .createNativeQuery(getResetGammaIdTableSql());
                nativeQuery1.executeUpdate();
                NativeQuery<?> nativeQuery2 = session
                        .createNativeQuery(getWellIdTableSql());
                nativeQuery2.executeUpdate();
                NativeQuery<?> nativeQuery3 = session
                        .createNativeQuery(getResetDownholeIdTableSql());
                nativeQuery3.executeUpdate();

                session.getTransaction().commit();
            } catch (Exception e) {
                log.info("Information: " + e);
            } finally {
                sessionFactory.close();
            }
        }
    }

    private static Directional getObject() {
        return  Directional.builder()
                .measureDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(221.22)
                .gx(222.22)
                .gy(223.22)
                .gz(224.22)
                .bx(225.22)
                .by(226.22)
                .bz(227.22)
                .inc(284.22)
                .azTrue(229.22)
                .azMag(231.22)
                .azCorr(232.22)
                .toolfaceCorr(242.22)
                .downholeData(downholeData)
                .build();
    }
    private static WellData getwelldataObject() {
        return WellData.builder()
                .companyName("Test")
                .fieldName("Test")
                .wellCluster("Test")
                .well("Test")
                .build();
    }

    private static DownholeData getDownholeData() {
        return DownholeData.builder()
                .wellData(wellData)
                .build();
    }

    @Test
    void save() {
        Directional test = getObject();
        saved = directionalDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getBx(), saved.getBx());
    }

    @Test
    void findAll() {
        Directional test = getObject();
        saved = directionalDao.save(test);
        List<Directional> list = directionalDao.findAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findAllByDownholeId() {
        Directional test = getObject();
        saved = directionalDao.save(test);
        Integer downholeDataId = downholeData.getId();
        List<Directional> list = directionalDao.findAllByDownholeId(downholeDataId);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        Directional test = getObject();
        saved = directionalDao.save(test);
        Optional<Directional> optional = directionalDao.findById(saved.getId());
        assertTrue(optional.isPresent());
        Directional find = optional.get();
        assertEquals(saved.getId(), find.getId());
        assertEquals(test.getMeasuredDepth(), find.getMeasuredDepth());
    }

    @Test
    void update() {
        Directional test = getObject();
        saved = directionalDao.save(test);
        Double updateWell = 999.99;
        saved.setMeasuredDepth(updateWell);
        boolean updated = directionalDao.update(saved);
        assertTrue(updated);
        Optional<Directional> find = directionalDao.findById(saved.getId());
        assertTrue(find.isPresent());
        assertEquals(updateWell, find.get().getMeasuredDepth());
    }

    @Test
    void delete() {
        Directional test = getObject();
        saved = directionalDao.save(test);

        boolean deleted = directionalDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

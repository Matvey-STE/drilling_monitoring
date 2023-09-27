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
import org.matveyvs.entity.Gamma;
import org.matveyvs.entity.WellData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GammaDaoTest {
    private SessionFactory sessionFactory;
    private final GammaDao gammaDao = GammaDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Gamma saved;
    private static DownholeData downholeData;
    private static WellData wellData;
    private Integer downholeDbSize;
    private Integer wellDataDbSize;
    private Integer gammaDbSize;

    private String getResetDownholeIdTableSql() {
        return "ALTER SEQUENCE drilling.public.downhole_data_id_seq RESTART WITH " + downholeDbSize;
    }

    private String getWellIdTableSql() {
        return "ALTER SEQUENCE drilling.public.well_data_id_seq RESTART WITH " + wellDataDbSize;
    }

    private String getResetGammaIdTableSql() {
        return "ALTER SEQUENCE drilling.public.gamma_new_id_seq RESTART WITH " + gammaDbSize;
    }


    @BeforeEach
    void setUp() {
        gammaDbSize = gammaDao.findAll().size() + 1;
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
            gammaDao.delete(saved.getId());
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

    private static Gamma getObject() {
        return Gamma.builder()
                .measureDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(22.22)
                .grcx(22.22)
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
        Gamma test = getObject();
        saved = gammaDao.save(test);
        log.info("Entity {} was saved in test", saved);
        assertNotNull(saved);
        assertEquals(test.getGrcx(), saved.getGrcx());
    }

    @Test
    void findAll() {
        Gamma test = getObject();
        saved = gammaDao.save(test);
        List<Gamma> list = gammaDao.findAll();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void findById() {
        Gamma test = getObject();
        saved = gammaDao.save(test);
        Optional<Gamma> optional = gammaDao.findById(saved.getId());
        assertTrue(optional.isPresent());
        Gamma find = optional.get();
        assertEquals(saved.getId(), find.getId());
        assertEquals(test.getMeasuredDepth(), find.getMeasuredDepth());
    }

    @Test
    void update() {
        Gamma test = getObject();
        saved = gammaDao.save(test);
        Double updateWell = 999.99;
        saved.setGrcx(updateWell);
        boolean updated = gammaDao.update(saved);
        assertTrue(updated);
        Optional<Gamma> find = gammaDao.findById(saved.getId());
        assertTrue(find.isPresent());
        assertEquals(updateWell, find.get().getGrcx());
    }

    @Test
    void delete() {
        Gamma test = getObject();
        saved = gammaDao.save(test);

        boolean deleted = gammaDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

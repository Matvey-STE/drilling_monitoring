package org.matveyvs.dao;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.entity.Directional;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;
import org.matveyvs.utils.HibernateUtil;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DirectionalDaoTest {
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final DirectionalDao directionalDao = DirectionalDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Directional saved;
    private static DownholeData downholeData;
    private static WellData wellData;


    @BeforeEach
    void setUp() {
        wellData = wellDataDao.save(getwelldataObject());
        downholeData = downholeDataDao.save(getDownholeData());

        //create random data
        TestDatabaseUtil.createRandomData();
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
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static Directional getObject() {
        return Directional.builder()
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

    @Test
    void findAllDirectionalByFieldName() {
        String fieldName = "";
        if (directionalDao.findById(1).isPresent()) {
            fieldName = directionalDao.findById(1).get()
                    .getDownholeData().getWellData().getFieldName();
        }
        @Cleanup Session session = sessionFactory.openSession();
        List<Directional> fieldNameTest = directionalDao.findAllDirectionalByFieldName(session, fieldName);

        for (Directional directional : fieldNameTest) {
            assertEquals(directional.getDownholeData().getWellData().getFieldName(), fieldName);
        }
    }

    @Test
    void findAllDirByDepthBetweenAndFieldName() {
        String fieldName = "";
        Double depth = null;
        if (directionalDao.findById(1).isPresent()) {
            fieldName = directionalDao.findById(1).get()
                    .getDownholeData().getWellData().getFieldName();

            depth = directionalDao.findById(1).get().getMeasuredDepth();
        }
        Double startDepth = depth - 0.01;
        Double endDepth = depth + 0.01;
        @Cleanup Session session = sessionFactory.openSession();
        List<Directional> fieldNameTest = directionalDao
                .findAllDirByDepthBetweenAndFieldName(session, fieldName, startDepth, endDepth);

        for (Directional directional : fieldNameTest) {
            assertEquals(directional.getDownholeData().getWellData().getFieldName(), fieldName);
            assertTrue(directional.getMeasuredDepth() > startDepth);
            assertTrue(directional.getMeasuredDepth() < endDepth);
            System.out.println(directional);
        }
    }

}

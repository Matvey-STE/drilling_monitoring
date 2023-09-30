package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
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
    private final GammaDao gammaDao = GammaDao.getInstance();
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private Gamma saved;
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
            gammaDao.delete(saved.getId());
            downholeDataDao.delete(downholeData.getId());
            wellDataDao.delete(wellData.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
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
    void findAllByDownholeId() {
        Gamma test = getObject();
        saved = gammaDao.save(test);
        Integer downholeDataId = downholeData.getId();
        List<Gamma> list = gammaDao.findAllByDownholeId(downholeDataId);
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

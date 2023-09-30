package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DownholeDataDaoTest {
    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private DownholeData saved;
    private static WellData wellData;


    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();

        wellData = wellDataDao.save(getwelldataObject());
    }

    @AfterEach
    void tearDown() {
        try {
            downholeDataDao.delete(saved.getId());
            wellDataDao.delete(wellData.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
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
    void findAllByWellId() {
        DownholeData test = getObject();
        saved = downholeDataDao.save(test);
        Integer wellDataId = wellData.getId();
        List<DownholeData> list = downholeDataDao.findAllByWellId(wellDataId);
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

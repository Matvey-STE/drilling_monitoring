package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.entity.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WellDataDaoTest {
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private WellData saved;

    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();
    }

    @AfterEach
    void tearDown() {
        try {
            wellDataDao.delete(saved.getId());
        } catch (Exception e) {
            log.info("Entity was deleted earlier " + e);
        }
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
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
        saved = wellDataDao.save(getObject());
        boolean deleted = wellDataDao.delete(saved.getId());
        assertTrue(deleted);
    }
}

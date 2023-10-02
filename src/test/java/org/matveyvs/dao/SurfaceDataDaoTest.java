package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.entity.WellData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class SurfaceDataDaoTest {
    private final SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();
    private SurfaceData saved;
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
            surfaceDataDao.delete(saved.getId());
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
    void findAllByWelldataId() {
        SurfaceData test = getObject();
        saved = surfaceDataDao.save(test);
        Integer wellDataId = wellData.getId();
        List<SurfaceData> list = surfaceDataDao.findAllByWelldataId(wellDataId);
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

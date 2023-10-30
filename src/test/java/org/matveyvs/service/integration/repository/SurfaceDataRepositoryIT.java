package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.entity.WellData;
import org.matveyvs.repository.SurfaceDataRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@IT
@AllArgsConstructor
class SurfaceDataRepositoryIT {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private SurfaceDataRepository surfaceDataRepository;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @Test
    void create() {
        WellData wellData = WellData.builder()
                .id(1)
                .fieldName("Field Name")
                .well("Well")
                .build();
        SurfaceData surfaceData = SurfaceData.builder()
                .measuredDate(Timestamp.valueOf(LocalDateTime.now()))
                .wob(22.22)
                .measuredDepth(22.22)
                .wellData(wellData)
                .build();
        SurfaceData save = surfaceDataRepository.save(surfaceData);
        assertEquals("surface data test", save.getWob(), surfaceData.getWob());
    }

    @Test
    void findAll() {
        List<SurfaceData> all = surfaceDataRepository.findAll();
        assertFalse("find all surface data", all.isEmpty());
    }
}

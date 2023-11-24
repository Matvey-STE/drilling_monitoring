package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.entity.WellData;
import org.matveyvs.repository.SurfaceDataRepository;
import org.matveyvs.repository.WellDataRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.*;

@IT
@AllArgsConstructor
class SurfaceDataRepositoryIT {
    private WellDataRepository wellDataRepository;
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private SurfaceDataRepository surfaceDataRepository;

    @BeforeEach
    void setUp() {
        if (surfaceDataRepository.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void create() {
        Optional<WellData> optional = wellDataRepository.findById(1);
        SurfaceData surfaceData = SurfaceData.builder()
                .measuredDate(Timestamp.valueOf(LocalDateTime.now()))
                .wob(22.22)
                .measuredDepth(22.22)
                .wellData(optional.orElse(null))
                .build();
        SurfaceData save = surfaceDataRepository.save(surfaceData);
        assertEquals("surface data test", save.getWob(), surfaceData.getWob());
        surfaceDataRepository.delete(save);
    }

    @Test
    void findAll() {
        List<SurfaceData> all = surfaceDataRepository.findAll();
        assertFalse("find all surface data", all.isEmpty());
    }
}

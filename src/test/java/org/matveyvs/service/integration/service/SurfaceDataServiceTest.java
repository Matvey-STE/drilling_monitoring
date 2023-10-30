package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.SurfaceDataCreateDto;
import org.matveyvs.dto.SurfaceDataReadDto;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.service.SurfaceDataService;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@IT
@AllArgsConstructor
class SurfaceDataServiceTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private WellDataService wellDataService;
    private SurfaceDataService surfaceDataService;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        var wellDataCreateDto = new WellDataCreateDto(
                "company name",
                "field name",
                "well cluster",
                "well");
        Integer integer = wellDataService.create(wellDataCreateDto);
        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        var surfaceDataDto = new SurfaceDataCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                22.22,
                22.22,
                22.22,
                22.22,
                22.22,
                22.22,
                22.22,
                byId.get());
        Integer integer1 = surfaceDataService.create(surfaceDataDto);
        Optional<SurfaceDataReadDto> byId1 = surfaceDataService.findById(integer1);
        System.out.println(byId1);
    }

    @Test
    void findAllByWellId() {
        List<SurfaceDataReadDto> allByWellId = surfaceDataService.findAllByWellId(20);
        System.out.println(allByWellId);
    }
}
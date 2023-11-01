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

import static org.junit.jupiter.api.Assertions.*;

@IT
class SurfaceDataServiceTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final SurfaceDataService surfaceDataService;
    private final WellDataService wellDataService;
    private WellDataReadDto wellDataReadDto;

    public SurfaceDataServiceTest(RandomWellDataBaseCreator randomWellDataBaseCreator,
                                  SurfaceDataService surfaceDataService,
                                  WellDataService wellDataService) {
        this.randomWellDataBaseCreator = randomWellDataBaseCreator;
        this.surfaceDataService = surfaceDataService;
        this.wellDataService = wellDataService;
    }

    @BeforeEach
    void setUp() {
        if (wellDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        randomWellDataBaseCreator.createRandomDataForTests();
        Integer integer = wellDataService.create(getWellDataDto());
        wellDataReadDto = wellDataService.findById(integer).orElse(null);
    }

    @AfterEach
    void tearDown() {
    }

    private WellDataCreateDto getWellDataDto() {
        return new WellDataCreateDto(
                "Company Name",
                "Field Name",
                "Well Cluster",
                "Well");
    }

    private SurfaceDataCreateDto getSurfaceDataDto() {
        return new SurfaceDataCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                21.22,
                22.22,
                22.22,
                22.22,
                22.22,
                22.22,
                22.22,
                wellDataReadDto);
    }

    @Test
    void create() {
        Integer integer1 = surfaceDataService.create(getSurfaceDataDto());
        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(integer1);
        assertTrue(byId.isPresent());
        byId.ifPresent(surfaceDataReadDto ->
                assertEquals(surfaceDataReadDto.measuredDepth(), getSurfaceDataDto().measuredDepth()));

    }

    @Test
    void update() {
        var entity = getSurfaceDataDto();
        Integer integer = surfaceDataService.create(entity);
        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(integer);
        assertTrue(byId.isPresent());
        var entityToUpdate = new SurfaceDataReadDto(
                integer,
                Timestamp.valueOf(LocalDateTime.now()),
                33.22,
                34.22,
                45.22,
                36.22,
                37.22,
                38.22,
                39.22,
                wellDataReadDto);
        boolean update = surfaceDataService.update(entityToUpdate);
        assertTrue(update);

        var updatedEntity = surfaceDataService.findById(integer);
        assertTrue(updatedEntity.isPresent());
        updatedEntity.ifPresent(entityToCompare -> assertEquals(entityToCompare.measuredDepth(), entityToUpdate.measuredDepth()));
        boolean delete = surfaceDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findById() {
        var entity = getSurfaceDataDto();
        Integer integer = surfaceDataService.create(entity);

        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = surfaceDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findAll() {
        var entity = getSurfaceDataDto();
        Integer integer = surfaceDataService.create(entity);

        List<SurfaceDataReadDto> all = surfaceDataService.findAll();
        assertFalse(all.isEmpty());
        boolean delete = surfaceDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void delete() {
        var entity = getSurfaceDataDto();
        Integer integer = surfaceDataService.create(entity);
        boolean delete = surfaceDataService.delete(integer);
        assertTrue(delete);
    }


    @Test
    void findAllByWellId() {
        Integer integer = surfaceDataService.create(getSurfaceDataDto());
        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(integer);
        int id = 0;
        if (byId.isPresent()) {
            id = byId.get().wellDataReadDto().id();
        }
        List<SurfaceDataReadDto> allByWellId = surfaceDataService.findAllByWellId(id);
        assertFalse(allByWellId.isEmpty());
        boolean delete = surfaceDataService.delete(integer);
        assertTrue(delete);
    }
}
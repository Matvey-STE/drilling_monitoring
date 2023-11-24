package org.matveyvs.service.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.*;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class GammaServiceTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final GammaService gammaService;
    private final WellDataService wellDataService;
    private final DownholeDataService downholeDataService;
    private WellDataReadDto wellDataReadDto;
    private DownholeDataReadDto downholeDataReadDto;

    @BeforeEach
    void setUp() {
        if (wellDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        var wellId = wellDataService.create(getWellDataDto());
        wellDataReadDto = wellDataService.findById(wellId).orElse(null);
        var downholeId = downholeDataService.create(getDownholeDataDto());
        downholeDataReadDto = downholeDataService.findById(downholeId).orElse(null);
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

    private DownholeDataCreateDto getDownholeDataDto() {
        return new DownholeDataCreateDto(
                wellDataReadDto);
    }

    private GammaCreateDto getGammaCreateDto() {
        return new GammaCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                33.33,
                33.33,
                downholeDataReadDto
        );
    }

    @Test
    void create() {
        Integer integer1 = gammaService.create(getGammaCreateDto());
        var byId = gammaService.findById(integer1);
        assertTrue(byId.isPresent());
        byId.ifPresent(gammaReadDto ->
                assertEquals(gammaReadDto.measuredDepth(),
                        getGammaCreateDto().measuredDepth()));

    }

    @Test
    void update() {
        var entity = getGammaCreateDto();
        Integer integer = gammaService.create(entity);
        var byId = gammaService.findById(integer);
        assertTrue(byId.isPresent());
        var entityToUpdate = new GammaReadDto(
                integer,
                Timestamp.valueOf(LocalDateTime.now()),
                44.33,
                44.44,
                downholeDataReadDto);
        boolean update = gammaService.update(entityToUpdate);
        assertTrue(update);

        var updatedEntity = gammaService.findById(integer);
        assertTrue(updatedEntity.isPresent());
        updatedEntity.ifPresent(entityToCompare -> assertEquals(entityToCompare.grcx(),
                entityToUpdate.grcx()));
        boolean delete = gammaService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findById() {
        var entity = getGammaCreateDto();
        Integer integer = gammaService.create(entity);

        var byId = gammaService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = gammaService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findAll() {
        var entity = getGammaCreateDto();
        Integer integer = gammaService.create(entity);

        var all = gammaService.findAll();
        assertFalse(all.isEmpty());
        boolean delete = gammaService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void delete() {
        var entity = getGammaCreateDto();
        Integer integer = gammaService.create(entity);
        boolean delete = gammaService.delete(integer);
        assertTrue(delete);
    }


    @Test
    void findAllByDownholeDataId() {
        Integer integer = gammaService.create(getGammaCreateDto());
        var byId = gammaService.findById(integer);
        int id = 0;
        if (byId.isPresent()) {
            id = byId.get().downholeDataReadDto().id();
        }
        var allByWellId = gammaService.findAllByDownholeId(id);
        assertFalse(allByWellId.isEmpty());
        boolean delete = gammaService.delete(integer);
        assertTrue(delete);
    }
}
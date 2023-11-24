package org.matveyvs.service.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.*;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class DirectionalServiceTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final DirectionalService directionalService;
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

    private DirectionalCreateDto getDirectionalDto() {
        return new DirectionalCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                33.33,
                33.33,
                31.33,
                32.33,
                33.33,
                34.33,
                35.33,
                36.33,
                33.33,
                33.33,
                33.33,
                33.33,
                downholeDataReadDto
        );
    }

    @Test
    void create() {
        Integer integer1 = directionalService.create(getDirectionalDto());
        var byId = directionalService.findById(integer1);
        assertTrue(byId.isPresent());
        byId.ifPresent(gammaReadDto ->
                assertEquals(gammaReadDto.measuredDepth(),
                        getDirectionalDto().measuredDepth()));

    }

    @Test
    void update() {
        var entity = getDirectionalDto();
        Integer integer = directionalService.create(entity);
        var byId = directionalService.findById(integer);
        assertTrue(byId.isPresent());
        var entityToUpdate = new DirectionalReadDto(
                integer,
                Timestamp.valueOf(LocalDateTime.now()),
                44.44,
                33.33,
                31.33,
                32.33,
                33.33,
                34.33,
                35.33,
                36.33,
                33.33,
                33.33,
                33.33,
                33.33,
                downholeDataReadDto
        );
        boolean update = directionalService.update(entityToUpdate);
        assertTrue(update);

        var updatedEntity = directionalService.findById(integer);
        assertTrue(updatedEntity.isPresent());
        updatedEntity.ifPresent(entityToCompare -> assertEquals(entityToCompare.measuredDepth(),
                entityToUpdate.measuredDepth()));
        boolean delete = directionalService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findById() {
        var entity = getDirectionalDto();
        Integer integer = directionalService.create(entity);

        var byId = directionalService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = directionalService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findAll() {
        var entity = getDirectionalDto();
        Integer integer = directionalService.create(entity);

        var all = directionalService.findAll();
        assertFalse(all.isEmpty());
        boolean delete = directionalService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void delete() {
        var entity = getDirectionalDto();
        Integer integer = directionalService.create(entity);
        boolean delete = directionalService.delete(integer);
        assertTrue(delete);
    }


    @Test
    void findAllByDownholeDataId() {
        Integer integer = directionalService.create(getDirectionalDto());
        var byId = directionalService.findById(integer);
        int id = 0;
        if (byId.isPresent()) {
            id = byId.get().downholeDataReadDto().id();
        }
        var allByWellId = directionalService.findAllByDownholeId(id);
        assertFalse(allByWellId.isEmpty());
        boolean delete = directionalService.delete(integer);
        assertTrue(delete);
    }
}
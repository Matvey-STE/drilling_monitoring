package org.matveyvs.service.integration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.*;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.service.WellDataService;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
class DownholeDataServiceTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final DownholeDataService downholeDataService;
    private final WellDataService wellDataService;
    private WellDataReadDto wellDataReadDto;

    public DownholeDataServiceTest(RandomWellDataBaseCreator randomWellDataBaseCreator,
                                   DownholeDataService downholeDataService,
                                   WellDataService wellDataService) {
        this.randomWellDataBaseCreator = randomWellDataBaseCreator;
        this.downholeDataService = downholeDataService;
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

    private DownholeDataCreateDto getSurfaceDataDto() {
        return new DownholeDataCreateDto(
                wellDataReadDto);
    }

    @Test
    void create() {
        Integer integer1 = downholeDataService.create(getSurfaceDataDto());
        var byId = downholeDataService.findById(integer1);
        assertTrue(byId.isPresent());
        byId.ifPresent(surfaceDataReadDto ->
                assertEquals(surfaceDataReadDto.wellDataReadDto(),
                        getSurfaceDataDto().wellDataReadDto()));

    }

    @Test
    void update() {
        var entity = getSurfaceDataDto();
        Integer integer = downholeDataService.create(entity);
        var byId = downholeDataService.findById(integer);
        assertTrue(byId.isPresent());
        var entityToUpdate = new DownholeDataReadDto(
                integer,
                wellDataReadDto);
        boolean update = downholeDataService.update(entityToUpdate);
        assertTrue(update);

        var updatedEntity = downholeDataService.findById(integer);
        assertTrue(updatedEntity.isPresent());
        updatedEntity.ifPresent(entityToCompare -> assertEquals(entityToCompare.wellDataReadDto(),
                entityToUpdate.wellDataReadDto()));
        boolean delete = downholeDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findById() {
        var entity = getSurfaceDataDto();
        Integer integer = downholeDataService.create(entity);

        var byId = downholeDataService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = downholeDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findAll() {
        var entity = getSurfaceDataDto();
        Integer integer = downholeDataService.create(entity);

        var all = downholeDataService.findAll();
        assertFalse(all.isEmpty());
        boolean delete = downholeDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void delete() {
        var entity = getSurfaceDataDto();
        Integer integer = downholeDataService.create(entity);
        boolean delete = downholeDataService.delete(integer);
        assertTrue(delete);
    }


    @Test
    void findAllByWellId() {
        Integer integer = downholeDataService.create(getSurfaceDataDto());
        var byId = downholeDataService.findById(integer);
        int id = 0;
        if (byId.isPresent()) {
            id = byId.get().wellDataReadDto().id();
        }
        var allByWellId = downholeDataService.findAllByWellId(id);
        assertFalse(allByWellId.isEmpty());
        boolean delete = downholeDataService.delete(integer);
        assertTrue(delete);
    }
}
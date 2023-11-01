package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.entity.Role;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@AllArgsConstructor
class WellDataServiceTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private WellDataService wellDataService;


    @BeforeEach
    void setUp() {
        if (wellDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
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

    @Test
    void create() {
        var well = getWellDataDto();
        Integer integer = wellDataService.create(well);
        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        assertTrue(byId.isPresent());
        byId.ifPresent(wellDataReadDto ->
                assertEquals(wellDataReadDto.companyName(), getWellDataDto().companyName()));
    }

    @Test
    void update() {
        var well = getWellDataDto();
        Integer integer = wellDataService.create(well);
        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        assertTrue(byId.isPresent());
        var entityToUpdate = new WellDataReadDto(
                integer,
                "update company name",
                "update field name",
                "update well cluster",
                "update well");
        boolean update = wellDataService.update(entityToUpdate);
        assertTrue(update);

        var updatedEntity = wellDataService.findById(integer);
        assertTrue(updatedEntity.isPresent());
        updatedEntity.ifPresent(wellDataReadDto -> assertEquals(wellDataReadDto.companyName(), entityToUpdate.companyName()));
        boolean delete = wellDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findById() {
        var well = getWellDataDto();
        Integer integer = wellDataService.create(well);

        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = wellDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void findAll() {
        var well = getWellDataDto();
        Integer integer = wellDataService.create(well);

        List<WellDataReadDto> all = wellDataService.findAll();
        assertFalse(all.isEmpty());
        boolean delete = wellDataService.delete(integer);
        assertTrue(delete);
    }

    @Test
    void delete() {
        var well = getWellDataDto();
        Integer integer = wellDataService.create(well);
        boolean delete = wellDataService.delete(integer);
        assertTrue(delete);
    }
}
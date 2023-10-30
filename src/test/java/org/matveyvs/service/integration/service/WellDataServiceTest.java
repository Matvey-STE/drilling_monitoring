package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

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
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
        List<WellDataReadDto> all = wellDataService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void delete() {
    }
}
package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.DownholeDataCreateDto;
import org.matveyvs.dto.DownholeDataReadDto;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.service.WellDataService;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.Optional;

@IT
@AllArgsConstructor
class DownholeDataServiceTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private WellDataService wellDataService;
    private DownholeDataService downholeDataService;

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
        var downholeDataCreateDto = new DownholeDataCreateDto(byId.get());
        Integer downholeDataId = downholeDataService.create(downholeDataCreateDto);
        Optional<DownholeDataReadDto> byId1 = downholeDataService.findById(downholeDataId);
        System.out.println(byId1);
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAllByWellId() {
    }
}
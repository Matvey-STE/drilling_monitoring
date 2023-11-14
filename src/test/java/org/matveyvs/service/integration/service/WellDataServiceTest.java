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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
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
        wellDataService.delete(integer);
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

    @Test
    void getWellDataPagesSortById() {
        var company = getWellDataDto();
        Integer integer = wellDataService.create(company);
        var sort = Sort.by("id");
        var pageRequest = PageRequest.of(0, 2, sort.descending());
        var wellDataPages = wellDataService.getWellDataPages(pageRequest);
        System.out.println("Well Data Pages:");
        for (Map.Entry<Integer, List<WellDataReadDto>> entry : wellDataPages.entrySet()) {
            System.out.println("Page " + entry.getKey() + ": " + entry.getValue());
        }
        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        assertEquals(wellDataPages.get(1).get(0).id(), byId.get().id(),
                "check first id element in list");
        wellDataService.delete(integer);
    }

    @Test
    void getWellDataPagesFromSecondPageSortByCompany() {
        var company = new WellDataCreateDto(
                "Zinger",
                "Field Name",
                "Well Cluster",
                "Well");
        Integer integer = wellDataService.create(company);
        var sort = Sort.by("companyName");
        var pageRequest = PageRequest.of(1, 2, sort);
        var wellDataPages = wellDataService.getWellDataPages(pageRequest);
        System.out.println("Well Data Pages:");
        for (Map.Entry<Integer, List<WellDataReadDto>> entry : wellDataPages.entrySet()) {
            System.out.println("Page " + entry.getKey() + ": " + entry.getValue());
        }
        Optional<WellDataReadDto> byId = wellDataService.findById(integer);
        List<WellDataReadDto> wellDataReadDtos = wellDataPages.get(1);
        assertEquals(wellDataReadDtos.get(1).companyName(), byId.get().companyName(),
                "check first id element in list");
        wellDataService.delete(integer);
    }
}
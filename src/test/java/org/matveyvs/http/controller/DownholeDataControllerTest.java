package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class DownholeDataControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final DownholeDataService downholeDataService;
    private final MockMvc mockMvc;
    private final WellDataService wellDataService;
    private static Integer wellDataId;

    @BeforeEach
    void setUp() {
        if (downholeDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        wellDataId = wellDataService.create(getWellDataDto());
    }

    private WellDataCreateDto getWellDataDto() {
        return new WellDataCreateDto(
                "Company Name",
                "Field Name",
                "Well Cluster",
                "Well");
    }

    @Test
    void showSurfaceData() throws Exception {
        mockMvc.perform(get("/wells/{id}/downhole", wellDataId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/downholeData"))
                .andExpect(model().attributeExists("downhole"))
                .andExpect(model().attributeExists("well"));
    }
}
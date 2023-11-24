package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.service.SurfaceDataService;
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
class SurfaceDataControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final SurfaceDataService surfaceDataService;
    private final WellDataService wellDataService;
    private final MockMvc mockMvc;
    private static Integer wellDataId;

    @BeforeEach
    void setUp() {
        if (surfaceDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        wellDataId = wellDataService.create(getWellDataDto());
    }

    @Test
    void showSurfaceData() throws Exception {
        mockMvc.perform(get("/wells/{id}/surface", wellDataId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/surfaceDataList"))
                .andExpect(model().attributeExists("surfaceData"))
                .andExpect(model().attributeExists("well"));
    }

    private WellDataCreateDto getWellDataDto() {
        return new WellDataCreateDto(
                "Company Name",
                "Field Name",
                "Well Cluster",
                "Well");
    }
}
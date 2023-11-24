package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.*;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class DirectionalControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final DirectionalService directionalService;
    private final MockMvc mockMvc;
    private final WellDataService wellDataService;
    private final DownholeDataService downholeDataService;
    private WellDataReadDto wellDataReadDto;
    private static Integer wellId;
    private static Integer downholeId;

    @BeforeEach
    void setUp() {
        if (directionalService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        wellId = wellDataService.create(getWellDataDto());
        wellDataReadDto = wellDataService.findById(wellId).orElse(null);
        downholeId = downholeDataService.create(getDownholeDataDto());
    }

    @Test
    void showDirectionalList() throws Exception {
        mockMvc.perform(get("/wells/{wellId}/downhole/{downholeId}/directional", wellId, downholeId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/directionalList"))
                .andExpect(model().attributeExists("directional"))
                .andExpect(model().attributeExists("well"));
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
}
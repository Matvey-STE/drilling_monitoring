package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.service.GammaService;
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
class GammaControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final GammaService gammaService;
    private final MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        if (gammaService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void showSurfaceData() throws Exception {
        var wellId = 1;
        var downholeId = 1;
        mockMvc.perform(get("/wells/{wellId}/downhole/{downholeId}/gamma", wellId, downholeId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/gammaList"))
                .andExpect(model().attributeExists("gamma"))
                .andExpect(model().attributeExists("well"));
    }
}
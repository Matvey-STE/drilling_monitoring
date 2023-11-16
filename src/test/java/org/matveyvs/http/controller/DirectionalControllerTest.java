package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.service.DirectionalService;
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
    @BeforeEach
    void setUp() {
        if (directionalService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void showDirectionalList() throws Exception {
        var wellId = 1;
        var downholeId = 1;
        mockMvc.perform(get("/wells/{wellId}/downhole/{downholeId}/directional", wellId, downholeId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/directionalList"))
                .andExpect(model().attributeExists("directional"))
                .andExpect(model().attributeExists("well"));
    }
}
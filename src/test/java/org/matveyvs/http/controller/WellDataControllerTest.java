package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.service.WellDataService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.matveyvs.dto.WellDataCreateDto.Fields.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class WellDataControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final WellDataService wellDataService;
    private final MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        if (wellDataService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void showWellsPage() throws Exception {
        mockMvc.perform(get("/wells"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/wells"))
                .andExpect(model().attributeExists("welldata"));

    }

    @Test
    void createWell() throws Exception {
        mockMvc.perform(get("/wellAdd"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/monitoring/wellAdd"))
                .andExpect(model().attributeExists("well"));

    }

    @Test
    void createWellPost() throws Exception {
        mockMvc.perform(post("/wellAdd")
                        .param(companyName, "Company Name")
                        .param(fieldName, "Field Name")
                        .param(wellCluster, "Well Cluster")
                        .param(well, "Well"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/wells")
                );
    }

    @Test
    void detailsOrEditById() throws Exception {
        var failUserId = -1;
        var successUserId = 1;
        mockMvc.perform(get("/wellEdit/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(get("/wellEdit/{id}", successUserId))
                .andExpect(model().attributeExists("well"))
                .andExpect(view().name("/monitoring/wellEdit"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/wellDetails/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(get("/wellDetails/{id}", successUserId))
                .andExpect(model().attributeExists("well"))
                .andExpect(view().name("/monitoring/wellDetails"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        var wellId = 1;
        mockMvc.perform(post("/wells/{id}/update", wellId)
                        .param("companyName", "update Company")
                        .param("well", "update Well"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/wellEdit/{\\d+}")
                );
    }

    @Test
    void delete() throws Exception {
        var failUserId = -1;
        var successUserId = 1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/wells/{id}/delete", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(MockMvcRequestBuilders.delete("/wells/{id}/delete", successUserId))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/wells")
                );
    }
}
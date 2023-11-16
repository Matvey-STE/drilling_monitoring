package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.service.UserService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.matveyvs.dto.UserCreateDto.Fields.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest {
    private final RandomWellDataBaseCreator randomWellDataBaseCreator;
    private final UserService userService;
    private final MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        if (userService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllPages() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/admin/userList"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sortField"))
                .andExpect(model().attributeExists("sortDir"))
                .andExpect(model().attributeExists("reverseSortDir"))
                .andExpect(model().attributeExists("keyword"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void getAllWithSort() throws Exception {
        int pageNumber = 1;
        mockMvc.perform(get("/users/page/{pageNumber}", pageNumber)
                        .flashAttr("pageNumber", 1)
                        .param("sortDir", "ASC")
                        .param("sortField", "id")
                        .param("keyword", ""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/admin/userList"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("sortField"))
                .andExpect(model().attributeExists("sortDir"))
                .andExpect(model().attributeExists("reverseSortDir"))
                .andExpect(model().attributeExists("keyword"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void createUserGet() throws Exception {
        mockMvc.perform(get("/userAdd"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/admin/userAdd"));

    }

    @Test
    void createUserPost() throws Exception {
        mockMvc.perform(post("/userAdd")
                        .param(username, "UserName")
                        .param(email, "test@mail.ru")
                        .param(password, "123")
                        .param(role, "ADMIN")
                        .param(firstName, "First Name")
                        .param(lastName, "Last Name"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    @Test
    void update() throws Exception {
        var userId = 1;
        mockMvc.perform(post("/users/{id}/update", userId)
                        .param("username", "update username")
                        .param("roles", "ADMIN"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/userEdit/{\\d+}")
                );
    }

    @Test
    void delete() throws Exception {
        var failUserId = -1;
        var successUserId = 1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", successUserId))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    @Test
    void detailsAndEditById() throws Exception {
        var failUserId = -1;
        var successUserId = 1;
        mockMvc.perform(get("/userEdit/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(get("/userEdit/{id}", successUserId))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("/admin/userEdit"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/userDetails/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mockMvc.perform(get("/userDetails/{id}", successUserId))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("/admin/userDetails"))
                .andExpect(status().is2xxSuccessful());
    }
}
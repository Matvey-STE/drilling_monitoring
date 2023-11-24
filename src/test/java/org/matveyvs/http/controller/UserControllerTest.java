package org.matveyvs.http.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
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
    private static Integer userId;


    @BeforeEach
    void setUp() {
        if (userService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
        userId = userService.create(getUser());
    }

    private UserCreateDto getUser() {
        return new UserCreateDto(
                "username service",
                "email@email.com",
                "password service",
                Role.USER,
                "Matvey",
                "Test");
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
                        .flashAttr("pageNumber", pageNumber)
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
    void deleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", userId))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    @Test
    void deleteFail() throws Exception {
        var failUserId = -1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}/delete", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    void detailsByIdSuccess() throws Exception {
        mockMvc.perform(get("/userDetails/{id}", userId)
                        .flashAttr("id", 1))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("/admin/userDetails"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void editByIdSuccess() throws Exception {
        mockMvc.perform(get("/userEdit/{successUserId}", userId)
                        .flashAttr("successUserId", userId))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("/admin/userEdit"))
                .andExpect(status().isOk());
    }

    @Test
    void detailsByIdFail() throws Exception {
        var failUserId = -1;
        mockMvc.perform(get("/userDetails/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    @Test
    void editByIdFail() throws Exception {
        var failUserId = -1;
        mockMvc.perform(get("/userEdit/{id}", failUserId))
                .andExpectAll(
                        status().is4xxClientError()
                );
    }
}
package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.entity.Role;
import org.matveyvs.service.UserService;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@AllArgsConstructor
class UserServiceTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private UserService userService;

    @BeforeEach
    void setUp() {
        if (userService.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @AfterEach
    void tearDown() {
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

    @Test
    void create() {
        var user = getUser();
        Integer integer = userService.create(user);

        Optional<UserReadDto> byId = userService.findById(integer);
        assertTrue(byId.isPresent());
        assertEquals(user.username(), byId.get().username());
        userService.delete(integer);
    }

    @Test
    void update() {
        var user = getUser();
        Integer integer = userService.create(user);

        Optional<UserReadDto> byId = userService.findById(integer);
        assertTrue(byId.isPresent());
        var userUpdate = new UserReadDto(
                integer,
                "update user service",
                "test@email.com",
                "update password service",
                Role.USER,
                "update first name",
                "update last name");
        var update = userService.update(userUpdate);
        assertTrue(update.isPresent());

        Optional<UserReadDto> updatedUser = userService.findById(integer);
        assertTrue(updatedUser.isPresent());
        assertEquals(updatedUser.get().username(), userUpdate.username());
        userService.delete(integer);
    }

    @Test
    void findById() {
        var user = getUser();
        Integer integer = userService.create(user);
        Optional<UserReadDto> byId = userService.findById(integer);
        assertTrue(byId.isPresent());
        userService.delete(integer);
    }

    @Test
    void findAll() {
        var user = getUser();
        Integer integer = userService.create(user);
        List<UserReadDto> all = userService.findAll();
        assertFalse(all.isEmpty());
        userService.delete(integer);
    }

    @Test
    void delete() {
        var user = getUser();
        Integer integer = userService.create(user);
        Optional<UserReadDto> byId = userService.findById(integer);
        assertTrue(byId.isPresent());
        boolean delete = userService.delete(integer);
        assertTrue(delete);
        Optional<UserReadDto> deletedUser = userService.findById(integer);
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void login() {
        var user = getUser();
        Integer integer = userService.create(user);
        Optional<UserReadDto> byId = userService.findById(integer);

        Optional<UserReadDto> login = userService.login(byId.get().email(), byId.get().password());
        assertTrue(login.isPresent());
        userService.delete(integer);
    }

    @Test
    void checkIfExist() {
        var user = getUser();
        Integer integer = userService.create(user);
        Optional<UserReadDto> byId = userService.findById(integer);

        boolean ifExist = userService.checkIfExist(byId.get().username(), byId.get().email());
        assertTrue(ifExist);
        userService.delete(integer);
    }
}
package org.matveyvs.service.integration.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

@IT
@AllArgsConstructor
class DirectionalServiceTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAllByDownholeId() {
    }
}
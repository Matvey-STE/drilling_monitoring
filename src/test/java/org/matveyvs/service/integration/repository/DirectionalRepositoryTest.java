package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Directional;
import org.matveyvs.repository.DirectionalRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertFalse;

@IT
@AllArgsConstructor
class DirectionalRepositoryTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private DirectionalRepository directionalRepository;

    @BeforeEach
    void setUp() {
        if (directionalRepository.findAll().isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void findAllByDownholeDataId() {
        List<Directional> allByDownholeDataId = directionalRepository.findAllByDownholeDataId(1);
        assertFalse("is not empty method", allByDownholeDataId.isEmpty());
    }
}
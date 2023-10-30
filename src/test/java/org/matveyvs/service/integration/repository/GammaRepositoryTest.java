package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.Gamma;
import org.matveyvs.repository.GammaRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertFalse;

@IT
@AllArgsConstructor
class GammaRepositoryTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private GammaRepository gammaRepository;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @Test
    void findAllByDownholeDataId() {
        List<Gamma> allByDownholeDataId = gammaRepository.findAllByDownholeDataId(1);
        assertFalse("all by downhole id is not empty", allByDownholeDataId.isEmpty());
    }
}
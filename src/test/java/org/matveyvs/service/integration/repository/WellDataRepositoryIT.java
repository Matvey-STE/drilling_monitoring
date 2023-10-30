package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.WellData;
import org.matveyvs.repository.WellDataRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@IT
@AllArgsConstructor
class WellDataRepositoryIT {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private WellDataRepository wellDataRepository;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @Test
    void create() {
        WellData wellData = WellData.builder()
                .fieldName("Field Name")
                .well("Well")
                .build();
        WellData save = wellDataRepository.save(wellData);
        assertEquals("create well data repository", save.getFieldName(), wellData.getFieldName());
    }

    @Test
    void findAll() {
        List<WellData> all = wellDataRepository.findAll();
        assertFalse("find all well data repository", all.isEmpty());
    }
}

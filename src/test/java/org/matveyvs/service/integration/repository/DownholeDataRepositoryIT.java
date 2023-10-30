package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.entity.WellData;
import org.matveyvs.repository.DownholeDataRepository;
import org.matveyvs.repository.WellDataRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;

@IT
@AllArgsConstructor
class DownholeDataRepositoryIT {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private DownholeDataRepository downholeDataRepository;
    private WellDataRepository wellDataRepository;

    @BeforeEach
    void setUp() {
        randomWellDataBaseCreator.createRandomDataForTests();
    }

    @Test
    void create() {
        var wellData = WellData
                .builder()
                .companyName("company name")
                .fieldName("field name")
                .wellCluster("well cluster")
                .well("well")
                .downholeDataList(new ArrayList<>())
                .build();
        WellData savedWellDate = wellDataRepository.save(wellData);
        var downholeData = DownholeData
                .builder()
                .wellData(savedWellDate)
                .build();

        DownholeData savedDownholeData = downholeDataRepository.save(downholeData);
        assertEquals("create downhole data repository",
                savedDownholeData.getWellData().getCompanyName(),
                wellData.getCompanyName());
    }

    @Test
    void findAll() {
        List<DownholeData> all = downholeDataRepository.findAll();
        assertFalse("all data is not empty", all.isEmpty());
    }
}

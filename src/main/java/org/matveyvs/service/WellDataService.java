package org.matveyvs.service;

import org.matveyvs.dao.WellDataDao;
import org.matveyvs.dto.WellDataDto;

import java.util.List;

public class WellDataService {
    private static final WellDataService INSTANCE = new WellDataService();
    private final WellDataDao wellDataDao = WellDataDao.getInstance();

    public List<WellDataDto> findAll() {
        return wellDataDao.findAll().stream().map(wellData ->
                        new WellDataDto(wellData.id(), wellData.companyName(), wellData.fieldName(),
                                wellData.wellCluster(), wellData.well(),
                                wellData.surfaceData().id(), wellData.downholeData().id()))
                .toList();
    }

    private WellDataService() {

    }

    public static WellDataService getInstance() {
        return INSTANCE;
    }
}


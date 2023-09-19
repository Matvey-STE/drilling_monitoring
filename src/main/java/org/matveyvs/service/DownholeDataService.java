package org.matveyvs.service;

import org.matveyvs.dao.DownholeDataDao;
import org.matveyvs.dto.DownholeDataDto;

import java.util.List;

public class DownholeDataService {
    private static final DownholeDataService INSTANCE = new DownholeDataService();

    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();


    public List<DownholeDataDto> findAllByWellId(Long wellDataId) {
        return downholeDataDao.findAllByWellId(wellDataId).stream()
                .map(downholeData -> new DownholeDataDto(downholeData.id(),
                        downholeData.wellData())).toList();
    }

    private DownholeDataService() {

    }

    public static DownholeDataService getInstance() {
        return INSTANCE;
    }
}

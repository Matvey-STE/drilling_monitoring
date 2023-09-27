package org.matveyvs.service;

import org.matveyvs.dao.DownholeDataDao;
import org.matveyvs.dto.DownholeDataDto;

import java.util.List;

public class DownholeDataService {
    private static final DownholeDataService INSTANCE = new DownholeDataService();

    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();


    public List<DownholeDataDto> findAllByWellId(Integer wellDataId) {
        return downholeDataDao.findAllByWellId(wellDataId).stream()
                .map(downholeData -> new DownholeDataDto(downholeData.getId(),
                        downholeData.getWellData())).toList();
    }

    private DownholeDataService() {

    }

    public static DownholeDataService getInstance() {
        return INSTANCE;
    }
}

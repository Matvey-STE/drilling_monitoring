package org.matveyvs.service;

import org.matveyvs.dao.DownholeDataDao;
import org.matveyvs.dto.DownholeDataDto;

import java.util.List;

public class DownholeDataService {
    private static final DownholeDataService INSTANCE = new DownholeDataService();

    private final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();

    public List<DownholeDataDto> findAll() {
        return downholeDataDao.findAll().stream()
                .map(downholeData -> new DownholeDataDto(downholeData.id(), downholeData.measuredDate(),
                        downholeData.directional(), downholeData.gamma())).toList();
    }

    private DownholeDataService() {

    }

    public static DownholeDataService getInstance() {
        return INSTANCE;
    }
}

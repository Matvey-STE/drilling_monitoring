package org.matveyvs.service;

import org.matveyvs.dao.SurfaceDataDao;
import org.matveyvs.dto.SurfaceDataDto;

import java.util.List;

public class SurfaceDataService {
    private static final SurfaceDataService INSTANCE = new SurfaceDataService();
    private final SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();

    public List<SurfaceDataDto> findAllByDownholeId(Integer downholeId) {
        return surfaceDataDao.findAllByDownholeId(downholeId).stream()
                .map(surfaceData -> new SurfaceDataDto(surfaceData.getId(), surfaceData.getMeasuredDate(),
                        surfaceData.getMeasuredDepth(), surfaceData.getHoleDepth(),
                        surfaceData.getTvDepth())).toList();
    }

    private SurfaceDataService() {

    }

    public static SurfaceDataService getInstance() {
        return INSTANCE;
    }
}

package org.matveyvs.service;

import org.matveyvs.dao.SurfaceDataDao;
import org.matveyvs.dto.SurfaceDataDto;

import java.util.List;

public class SurfaceDataService {
    private static final SurfaceDataService INSTANCE = new SurfaceDataService();
    private final SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();

    public List<SurfaceDataDto> findAll() {
        return surfaceDataDao.findAll().stream()
                .map(surfaceData -> new SurfaceDataDto(surfaceData.id(), surfaceData.measuredDate(),
                        surfaceData.measuredDepth(), surfaceData.holeDepth(),
                        surfaceData.tvDepth())).toList();
    }

    private SurfaceDataService() {

    }

    public static SurfaceDataService getInstance() {
        return INSTANCE;
    }
}

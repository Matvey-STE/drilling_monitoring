package org.matveyvs.service;

import org.matveyvs.dao.GammaDao;
import org.matveyvs.dto.GammaDto;

import java.util.List;

public class GammaService {

    private static final GammaService INSTANCE = new GammaService();
    private final GammaDao gammaDao = GammaDao.getInstance();

    public List<GammaDto> findAllByDownholeId(Long downholeId) {
        return gammaDao.findAllByDownholeId(downholeId).stream()
                .map(gamma -> new GammaDto(gamma.id(), gamma.timestamp(), gamma.measuredDepth(),
                        gamma.grcx(), gamma.downholeData())).toList();
    }

    private GammaService() {
    }

    public static GammaService getInstance() {
        return INSTANCE;
    }
}

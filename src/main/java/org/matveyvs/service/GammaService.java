package org.matveyvs.service;

import org.matveyvs.dao.GammaDao;
import org.matveyvs.dto.GammaDto;

import java.util.List;

public class GammaService {

    private static final GammaService INSTANCE = new GammaService();
    private final GammaDao gammaDao = GammaDao.getInstance();

    public List<GammaDto> findAllByDownholeId(Integer downholeId) {
        return gammaDao.findAllByDownholeId(downholeId).stream()
                .map(gamma -> new GammaDto(gamma.getId(), gamma.getMeasureDate(), gamma.getMeasuredDepth(),
                        gamma.getGrcx(), gamma.getDownholeData())).toList();
    }

    private GammaService() {
    }

    public static GammaService getInstance() {
        return INSTANCE;
    }
}

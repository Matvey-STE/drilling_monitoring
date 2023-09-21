package org.matveyvs.service;

import org.matveyvs.dao.DirectionalDao;
import org.matveyvs.dto.DirectionalDto;

import java.util.List;

public class DirectionalService {
    private static final DirectionalService INSTANCE = new DirectionalService();
    private final DirectionalDao directionalDao = DirectionalDao.getInstance();

    public List<DirectionalDto> findAllByDownholeId(Long downholeId) {
        return directionalDao.findAllByDownholeId(downholeId).stream()
                .map(directional -> new DirectionalDto(directional.id(), directional.measureDate(),
                        directional.measuredDepth(), directional.inc(), directional.azCorr(),
                        directional.azTrue(), directional.downholeData()))
                .toList();
    }

    private DirectionalService() {
    }

    public static DirectionalService getInstance() {
        return INSTANCE;
    }
}

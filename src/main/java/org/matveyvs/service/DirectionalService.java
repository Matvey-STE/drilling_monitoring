package org.matveyvs.service;

import org.matveyvs.dao.DirectionalDao;
import org.matveyvs.dto.DirectionalDto;

import java.util.List;

public class DirectionalService {
    private static final DirectionalService INSTANCE = new DirectionalService();
    private final DirectionalDao directionalDao = DirectionalDao.getInstance();

    public List<DirectionalDto> findAllByDownholeId(Integer downholeId) {
        return directionalDao.findAllByDownholeId(downholeId).stream()
                .map(directional -> new DirectionalDto(directional.getId(), directional.getMeasureDate(),
                        directional.getMeasuredDepth(), directional.getInc(), directional.getAzCorr(),
                        directional.getAzTrue(), directional.getDownholeData()))
                .toList();
    }

    private DirectionalService() {
    }

    public static DirectionalService getInstance() {
        return INSTANCE;
    }
}

package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.GammaRepository;
import org.matveyvs.dto.repo.*;
import org.matveyvs.entity.Gamma;
import org.matveyvs.mapper.repo.GammaMapperImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GammaService {
    private final GammaRepository gammaRepository;
    private final GammaMapperImpl gammaMapper;

    public Integer create(GammaCreateDto wellDataCreateDto) {
        var entity = gammaMapper.map(wellDataCreateDto);
        return gammaRepository.save(entity).getId();
    }

    public boolean update(GammaReadDto gammaReadDto) {
        var optional = gammaRepository.findById(gammaReadDto.id());
        if (optional.isPresent()) {
            Gamma entity = gammaMapper.mapFull(gammaReadDto);
            gammaRepository.update(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<GammaReadDto> findById(Integer id) {
        return gammaRepository.findById(id).map(gammaMapper::map);
    }

    public List<GammaReadDto> findAll() {
        return gammaRepository.findAll().stream().map(gammaMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = gammaRepository.findById(id);
        if (maybeUser.isPresent()) {
            gammaRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public List<GammaReadDto> findAllByDownholeId(Integer downholeId) {
        return gammaRepository.findAllByDownholeId(downholeId).stream()
                .map(gammaMapper::map).toList();
    }
}

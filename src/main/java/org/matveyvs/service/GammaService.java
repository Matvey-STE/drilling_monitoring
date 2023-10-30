package org.matveyvs.service;

import lombok.AllArgsConstructor;
import org.matveyvs.dto.GammaCreateDto;
import org.matveyvs.dto.GammaReadDto;
import org.matveyvs.entity.Gamma;
import org.matveyvs.mapper.GammaMapperImpl;
import org.matveyvs.repository.GammaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class GammaService {
    private final GammaRepository gammaRepository;
    private final GammaMapperImpl gammaMapper;

    public Integer create(GammaCreateDto gammaCreateDto) {
        var entity = gammaMapper.map(gammaCreateDto);
        return gammaRepository.save(entity).getId();
    }

    public boolean update(GammaReadDto gammaReadDto) {
        var optional = gammaRepository.findById(gammaReadDto.id());
        if (optional.isPresent()) {
            Gamma entity = gammaMapper.mapFull(gammaReadDto);
            gammaRepository.save(entity);
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
            gammaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<GammaReadDto> findAllByDownholeId(Integer downholeId) {
        return gammaRepository.findAllByDownholeDataId(downholeId).stream()
                .map(gammaMapper::map).toList();
    }
}

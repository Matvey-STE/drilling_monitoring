package org.matveyvs.service;

import lombok.AllArgsConstructor;
import org.matveyvs.dto.SurfaceDataCreateDto;
import org.matveyvs.dto.SurfaceDataReadDto;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.mapper.SurfaceDataMapperImpl;
import org.matveyvs.repository.SurfaceDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class SurfaceDataService {
    private final SurfaceDataRepository surfaceDataRepository;
    private final SurfaceDataMapperImpl surfaceDataMapper;

    public Integer create(SurfaceDataCreateDto surfaceDataCreateDto) {
        var entity = surfaceDataMapper.map(surfaceDataCreateDto);
        return surfaceDataRepository.save(entity).getId();
    }

    public boolean update(SurfaceDataReadDto surfaceDataReadDto) {
        var optional = surfaceDataRepository.findById(surfaceDataReadDto.id());
        if (optional.isPresent()) {
            SurfaceData entity = surfaceDataMapper.mapFull(surfaceDataReadDto);
            surfaceDataRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }
    @Transactional
    public Optional<SurfaceDataReadDto> findById(Integer id) {
        return surfaceDataRepository.findById(id).map(surfaceDataMapper::map);
    }

    @Transactional
    public List<SurfaceDataReadDto> findAll() {
        return surfaceDataRepository.findAll().stream().map(surfaceDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = surfaceDataRepository.findById(id);
        if (maybeUser.isPresent()) {
            surfaceDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SurfaceDataReadDto> findAllByWellId(Integer wellId) {
        return surfaceDataRepository.findAllByWellDataId(wellId).stream()
                .map(surfaceDataMapper::map).toList();
    }
}

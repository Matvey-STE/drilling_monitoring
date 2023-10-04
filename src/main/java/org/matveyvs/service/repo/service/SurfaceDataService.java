package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.SurfaceDataRepository;
import org.matveyvs.dto.repo.SurfaceDataCreateDto;
import org.matveyvs.dto.repo.SurfaceDataReadDto;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.mapper.repo.SurfaceDataMapperImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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
            surfaceDataRepository.update(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<SurfaceDataReadDto> findById(Integer id) {
        return surfaceDataRepository.findById(id).map(surfaceDataMapper::map);
    }

    public List<SurfaceDataReadDto> findAll() {
        return surfaceDataRepository.findAll().stream().map(surfaceDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = surfaceDataRepository.findById(id);
        if (maybeUser.isPresent()) {
            surfaceDataRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SurfaceDataReadDto> findAllByWellId(Integer wellId) {
        return surfaceDataRepository.findAllByWelldataId(wellId).stream()
                .map(surfaceDataMapper::map).toList();
    }
}

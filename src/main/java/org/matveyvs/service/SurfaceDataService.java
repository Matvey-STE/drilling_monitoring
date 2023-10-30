package org.matveyvs.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class SurfaceDataService {
    private final SurfaceDataRepository surfaceDataRepository;
    private final SurfaceDataMapperImpl surfaceDataMapper;

    public Integer create(SurfaceDataCreateDto surfaceDataCreateDto) {
        var entity = surfaceDataMapper.map(surfaceDataCreateDto);
        log.info("Create method: " + entity);
        return surfaceDataRepository.save(entity).getId();
    }

    public boolean update(SurfaceDataReadDto surfaceDataReadDto) {
        var optional = surfaceDataRepository.findById(surfaceDataReadDto.id());
        log.info("Update method: " + optional);
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
        log.info("Find by Id method");
        return surfaceDataRepository.findById(id).map(surfaceDataMapper::map);
    }

    public List<SurfaceDataReadDto> findAll() {
        log.info("Find all method");
        return surfaceDataRepository.findAll().stream().map(surfaceDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = surfaceDataRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            surfaceDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SurfaceDataReadDto> findAllByWellId(Integer wellId) {
        log.info("Find all by well data id method");
        return surfaceDataRepository.findAllByWellDataId(wellId).stream()
                .map(surfaceDataMapper::map).toList();
    }
}

package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.DownholeDataRepository;
import org.matveyvs.dto.repo.*;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.mapper.repo.DownholeDataMapperImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DownholeDataService {
    private final DownholeDataRepository downholeDataRepository;
    private final DownholeDataMapperImpl downholeDataMapper;

    public Integer create(DownholeDataCreateDto downholeDataCreateDto) {
        var entity = downholeDataMapper.map(downholeDataCreateDto);
        return downholeDataRepository.save(entity).getId();
    }

    public boolean update(DownholeDataReadDto downholeDataReadDto) {
        var optional = downholeDataRepository.findById(downholeDataReadDto.id());
        if (optional.isPresent()) {
            DownholeData entity = downholeDataMapper.mapFull(downholeDataReadDto);
            downholeDataRepository.update(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<DownholeDataReadDto> findById(Integer id) {
        return downholeDataRepository.findById(id).map(downholeDataMapper::map);
    }

    public List<DownholeDataReadDto> findAll() {
        return downholeDataRepository.findAll().stream().map(downholeDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = downholeDataRepository.findById(id);
        if (maybeUser.isPresent()) {
            downholeDataRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DownholeDataReadDto> findAllByWellId(Integer wellId) {
        return downholeDataRepository.findAllByWellId(wellId).stream()
                .map(downholeDataMapper::map).toList();
    }
}

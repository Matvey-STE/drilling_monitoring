package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.DirectionalRepository;
import org.matveyvs.dto.repo.*;
import org.matveyvs.dto.repo.DirectionalReadDto;
import org.matveyvs.entity.Directional;
import org.matveyvs.mapper.repo.DirectionalMapperImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DirectionalService {
    private final DirectionalRepository directionalRepository;
    private final DirectionalMapperImpl directionalMapper;

    public Integer create(DirectionalCreateDto directionalCreateDto) {
        var entity = directionalMapper.map(directionalCreateDto);
        return directionalRepository.save(entity).getId();
    }

    public boolean update(DirectionalReadDto directionalReadDto) {
        var optional = directionalRepository.findById(directionalReadDto.id());
        if (optional.isPresent()) {
            Directional entity = directionalMapper.mapFull(directionalReadDto);
            directionalRepository.update(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<DirectionalReadDto> findById(Integer id) {
        return directionalRepository.findById(id).map(directionalMapper::map);
    }

    public List<DirectionalReadDto> findAll() {
        return directionalRepository.findAll().stream().map(directionalMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = directionalRepository.findById(id);
        if (maybeUser.isPresent()) {
            directionalRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DirectionalReadDto> findAllByDownholeId(Integer downholeId) {
        return directionalRepository.findAllByDownholeId(downholeId).stream()
                .map(directionalMapper::map).toList();
    }
}

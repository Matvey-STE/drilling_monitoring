package org.matveyvs.service;

import lombok.AllArgsConstructor;
import org.matveyvs.dto.DirectionalCreateDto;
import org.matveyvs.dto.DirectionalReadDto;
import org.matveyvs.entity.Directional;
import org.matveyvs.mapper.DirectionalMapperImpl;
import org.matveyvs.repository.DirectionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
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
            directionalRepository.save(entity);
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
            directionalRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DirectionalReadDto> findAllByDownholeId(Integer downholeId) {
        return directionalRepository.findAllByDownholeDataId(downholeId).stream()
                .map(directionalMapper::map).toList();
    }
}

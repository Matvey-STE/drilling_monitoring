package org.matveyvs.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class DirectionalService {
    private DirectionalRepository directionalRepository;
    private DirectionalMapperImpl directionalMapper;

    public Integer create(DirectionalCreateDto directionalCreateDto) {
        var entity = directionalMapper.map(directionalCreateDto);
        log.info("Create method: " + entity);
        return directionalRepository.save(entity).getId();
    }

    public boolean update(DirectionalReadDto directionalReadDto) {
        var optional = directionalRepository.findById(directionalReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            Directional entity = directionalMapper.mapFull(directionalReadDto);
            directionalRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<DirectionalReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return directionalRepository.findById(id).map(directionalMapper::map);
    }

    public List<DirectionalReadDto> findAll() {
        log.info("Find all method");
        return directionalRepository.findAll().stream().map(directionalMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = directionalRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            directionalRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DirectionalReadDto> findAllByDownholeId(Integer downholeId) {
        log.info("Find all by downhole id method");
        return directionalRepository.findAllByDownholeDataId(downholeId).stream()
                .map(directionalMapper::map).toList();
    }
}

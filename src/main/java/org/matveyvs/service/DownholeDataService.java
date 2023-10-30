package org.matveyvs.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.dto.DownholeDataCreateDto;
import org.matveyvs.dto.DownholeDataReadDto;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.mapper.DownholeDataMapperImpl;
import org.matveyvs.repository.DownholeDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class DownholeDataService {
    private final DownholeDataRepository downholeDataRepository;
    private final DownholeDataMapperImpl downholeDataMapper;

    public Integer create(DownholeDataCreateDto downholeDataCreateDto) {
        var entity = downholeDataMapper.map(downholeDataCreateDto);
        log.info("Create method: " + entity);
        return downholeDataRepository.save(entity).getId();
    }

    public boolean update(DownholeDataReadDto downholeDataReadDto) {
        var optional = downholeDataRepository.findById(downholeDataReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            DownholeData entity = downholeDataMapper.mapFull(downholeDataReadDto);
            downholeDataRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<DownholeDataReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return downholeDataRepository.findById(id).map(downholeDataMapper::map);
    }

    public List<DownholeDataReadDto> findAll() {
        log.info("Find all method");
        return downholeDataRepository.findAll().stream().map(downholeDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = downholeDataRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            downholeDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DownholeDataReadDto> findAllByWellId(Integer wellId) {
        log.info("Find all by well data id method");
        return downholeDataRepository.findAllByWellDataId(wellId).stream()
                .map(downholeDataMapper::map).toList();
    }
}

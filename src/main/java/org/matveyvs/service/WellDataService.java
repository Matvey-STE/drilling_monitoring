package org.matveyvs.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.entity.WellData;
import org.matveyvs.mapper.WellDataMapperImpl;
import org.matveyvs.repository.WellDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class WellDataService {
    private WellDataRepository wellDataRepository;
    private WellDataMapperImpl wellDataMapper;

    public Integer create(WellDataCreateDto wellDataCreateDto) {
        var entity = wellDataMapper.map(wellDataCreateDto);
        log.info("Create method: " + entity);
        return wellDataRepository.save(entity).getId();
    }

    public boolean update(WellDataReadDto wellDataReadDto) {
        var optional = wellDataRepository.findById(wellDataReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            WellData entity = wellDataMapper.mapFull(wellDataReadDto);
            wellDataRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<WellDataReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return wellDataRepository.findById(id).map(wellDataMapper::map);
    }

    public List<WellDataReadDto> findAll() {
        log.info("Find all method");
        return wellDataRepository.findAll().stream().map(wellDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = wellDataRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            wellDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

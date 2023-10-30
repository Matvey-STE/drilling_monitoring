package org.matveyvs.service;

import lombok.AllArgsConstructor;
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
public class WellDataService {
    private final WellDataRepository wellDataRepository;
    private final WellDataMapperImpl wellDataMapper;

    public Integer create(WellDataCreateDto wellDataCreateDto) {
        var entity = wellDataMapper.map(wellDataCreateDto);
        return wellDataRepository.save(entity).getId();
    }

    public boolean update(WellDataReadDto wellDataReadDto) {
        var optional = wellDataRepository.findById(wellDataReadDto.id());
        if (optional.isPresent()) {
            WellData entity = wellDataMapper.mapFull(wellDataReadDto);
            wellDataRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }
    public Optional<WellDataReadDto> findById(Integer id) {
        return wellDataRepository.findById(id).map(wellDataMapper::map);
    }
    public List<WellDataReadDto> findAll() {
        return wellDataRepository.findAll().stream().map(wellDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = wellDataRepository.findById(id);
        if (maybeUser.isPresent()) {
            wellDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

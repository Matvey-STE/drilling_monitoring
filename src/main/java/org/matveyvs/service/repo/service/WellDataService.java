package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.WellDataRepository;
import org.matveyvs.dto.repo.WellDataCreateDto;
import org.matveyvs.dto.repo.WellDataReadDto;
import org.matveyvs.entity.WellData;
import org.matveyvs.mapper.repo.WellDataMapperImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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
            wellDataRepository.update(entity);
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
            wellDataRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }
}

package org.matveyvs.service;

import lombok.AllArgsConstructor;
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
            downholeDataRepository.save(entity);
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
            downholeDataRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<DownholeDataReadDto> findAllByWellId(Integer wellId) {
        return downholeDataRepository.findAllByWellDataId(wellId).stream()
                .map(downholeDataMapper::map).toList();
    }
}

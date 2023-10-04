package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.repository.SurfaceDataRepository;
import org.matveyvs.dto.repo.SurfaceDataCreateDto;
import org.matveyvs.dto.repo.SurfaceDataReadDto;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.mapper.repo.SurfaceDataMapperImpl;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SurfaceDataService {
    private final SurfaceDataRepository surfaceDataRepository;
    private final SurfaceDataMapperImpl surfaceDataMapper;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public Integer create(SurfaceDataCreateDto surfaceDataCreateDto) {
        var validator = validatorFactory.getValidator();
        var  validateResult = validator.validate(surfaceDataCreateDto);
        if (!validateResult.isEmpty()){
            throw new ConstraintViolationException(validateResult);
        }
        var entity = surfaceDataMapper.map(surfaceDataCreateDto);
        return surfaceDataRepository.save(entity).getId();
    }

    public boolean update(SurfaceDataReadDto surfaceDataReadDto) {
        var validator = validatorFactory.getValidator();
        var  validateResult = validator.validate(surfaceDataReadDto);
        if (!validateResult.isEmpty()){
            throw new ConstraintViolationException(validateResult);
        }
        var optional = surfaceDataRepository.findById(surfaceDataReadDto.id());
        if (optional.isPresent()) {
            SurfaceData entity = surfaceDataMapper.mapFull(surfaceDataReadDto);
            surfaceDataRepository.update(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<SurfaceDataReadDto> findById(Integer id) {
        return surfaceDataRepository.findById(id).map(surfaceDataMapper::map);
    }

    public List<SurfaceDataReadDto> findAll() {
        return surfaceDataRepository.findAll().stream().map(surfaceDataMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = surfaceDataRepository.findById(id);
        if (maybeUser.isPresent()) {
            surfaceDataRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public List<SurfaceDataReadDto> findAllByWellId(Integer wellId) {
        return surfaceDataRepository.findAllByWelldataId(wellId).stream()
                .map(surfaceDataMapper::map).toList();
    }
}

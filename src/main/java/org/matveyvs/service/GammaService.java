package org.matveyvs.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.dto.GammaCreateDto;
import org.matveyvs.dto.GammaReadDto;
import org.matveyvs.entity.Gamma;
import org.matveyvs.mapper.GammaMapperImpl;
import org.matveyvs.repository.GammaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class GammaService {
    private GammaRepository gammaRepository;
    private GammaMapperImpl gammaMapper;

    public Integer create(GammaCreateDto gammaCreateDto) {
        var entity = gammaMapper.map(gammaCreateDto);
        log.info("Create method: " + entity);
        return gammaRepository.save(entity).getId();
    }

    public boolean update(GammaReadDto gammaReadDto) {
        var optional = gammaRepository.findById(gammaReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            Gamma entity = gammaMapper.mapFull(gammaReadDto);
            gammaRepository.save(entity);
            return true;
        } else {
            return false;
        }
    }

    public Optional<GammaReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return gammaRepository.findById(id).map(gammaMapper::map);
    }

    public List<GammaReadDto> findAll() {
        log.info("Find all method");
        return gammaRepository.findAll().stream().map(gammaMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = gammaRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            gammaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<GammaReadDto> findAllByDownholeId(Integer downholeId) {
        log.info("Find all by downhole id method");
        return gammaRepository.findAllByDownholeDataId(downholeId).stream()
                .map(gammaMapper::map).toList();
    }
}

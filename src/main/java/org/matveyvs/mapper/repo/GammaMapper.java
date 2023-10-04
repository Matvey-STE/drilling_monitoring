package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.GammaCreateDto;
import org.matveyvs.dto.repo.GammaReadDto;
import org.matveyvs.entity.Gamma;

@Mapper(uses = {DownholeDataMapper.class, WellDataMapper.class})
public interface GammaMapper {
    @Mapping(target = "downholeDataReadDto.wellDataReadDto", source = "downholeData.wellData")
    @Mapping(target = "downholeDataReadDto", source = "downholeData")
    GammaReadDto map(Gamma gamma);

    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    Gamma map(GammaCreateDto gammaCreateDto);

    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    Gamma mapFull(GammaReadDto gammaReadDto);
}

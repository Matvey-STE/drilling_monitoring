package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.GammaCreateDto;
import org.matveyvs.dto.GammaReadDto;
import org.matveyvs.entity.Gamma;

@Mapper(componentModel = "Spring", uses = {DownholeDataMapper.class, WellDataMapper.class})
public interface GammaMapper {
    @Mapping(target = "downholeDataReadDto.wellDataReadDto", source = "downholeData.wellData")
    @Mapping(target = "downholeDataReadDto", source = "downholeData")
    GammaReadDto map(Gamma gamma);

    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    @Mapping(target = "downholeData.directionalList", ignore = true)
    @Mapping(target = "downholeData.gammaList", ignore = true)
    @Mapping(target = "id", ignore = true)
    Gamma map(GammaCreateDto gammaCreateDto);

    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    @Mapping(target = "downholeData.directionalList", ignore = true)
    @Mapping(target = "downholeData.gammaList", ignore = true)
    Gamma mapFull(GammaReadDto gammaReadDto);
}

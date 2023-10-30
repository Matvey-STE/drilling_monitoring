package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.DirectionalCreateDto;
import org.matveyvs.dto.DirectionalReadDto;
import org.matveyvs.entity.Directional;

@Mapper(componentModel = "Spring", uses = {DownholeDataMapper.class, WellDataMapper.class})
public interface DirectionalMapper {
    @Mapping(target = "downholeDataReadDto.wellDataReadDto", ignore = true)
    @Mapping(target = "downholeDataReadDto", source = "downholeData")
    DirectionalReadDto map(Directional directional);

    @Mapping(target = "downholeData.wellData", ignore = true)
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    @Mapping(target = "downholeData.directionalList", ignore = true)
    @Mapping(target = "downholeData.gammaList", ignore = true)
    @Mapping(target = "id", ignore = true)
    Directional map(DirectionalCreateDto directionalCreateDto);

    @Mapping(target = "downholeData.wellData", ignore = true)
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    @Mapping(target = "downholeData.directionalList", ignore = true)
    @Mapping(target = "downholeData.gammaList", ignore = true)
    Directional mapFull(DirectionalReadDto directionalReadDto);
}

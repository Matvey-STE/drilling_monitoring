package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.DirectionalCreateDto;
import org.matveyvs.dto.repo.DirectionalReadDto;
import org.matveyvs.entity.Directional;

@Mapper(uses = {DownholeDataMapper.class, WellDataMapper.class})
public interface DirectionalMapper {
    @Mapping(target = "downholeDataReadDto.wellDataReadDto", source = "downholeData.wellData")
    @Mapping(target = "downholeDataReadDto", source = "downholeData")
    DirectionalReadDto map(Directional directional);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    Directional map(DirectionalCreateDto directionalCreateDto);

    @Mapping(target = "downholeData.wellData", source = "downholeDataReadDto.wellDataReadDto")
    @Mapping(target = "downholeData", source = "downholeDataReadDto")
    Directional mapFull(DirectionalReadDto directionalReadDto);
}

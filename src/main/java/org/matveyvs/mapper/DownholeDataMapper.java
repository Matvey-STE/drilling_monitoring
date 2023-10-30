package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.DownholeDataCreateDto;
import org.matveyvs.dto.DownholeDataReadDto;
import org.matveyvs.entity.DownholeData;
@Mapper(componentModel = "Spring", uses = {WellDataMapper.class})
public interface DownholeDataMapper {
    @Mapping(target = "wellDataReadDto", source = "wellData")
    DownholeDataReadDto map(DownholeData downholeData);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    @Mapping(target = "directionalList", ignore = true)
    @Mapping(target = "gammaList", ignore = true)
    @Mapping(target = "id", ignore = true)
    DownholeData map(DownholeDataCreateDto downholeDataCreateDto);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    @Mapping(target = "directionalList", ignore = true)
    @Mapping(target = "gammaList", ignore = true)
    DownholeData mapFull(DownholeDataReadDto downholeDataReadDto);
}

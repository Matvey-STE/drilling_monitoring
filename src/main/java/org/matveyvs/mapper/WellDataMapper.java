package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.entity.WellData;

@Mapper(componentModel = "Spring", uses = {SurfaceDataMapper.class, DownholeDataMapper.class})
public interface WellDataMapper {

    WellDataReadDto map(WellData wellData);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "surfaceDataList", ignore = true)
    @Mapping(target = "downholeDataList", ignore = true)
    WellData map(WellDataCreateDto wellDataCreateDto);
    @Mapping(target = "surfaceDataList", ignore = true)
    @Mapping(target = "downholeDataList", ignore = true)
    WellData mapFull(WellDataReadDto wellDataReadDto);
}

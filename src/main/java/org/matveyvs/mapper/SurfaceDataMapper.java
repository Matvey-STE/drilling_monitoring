package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.SurfaceDataCreateDto;
import org.matveyvs.dto.SurfaceDataReadDto;
import org.matveyvs.entity.SurfaceData;

@Mapper(componentModel = "Spring", uses = {WellDataMapper.class})
public interface SurfaceDataMapper {
    @Mapping(target = "wellDataReadDto", source = "wellData")
    SurfaceDataReadDto map(SurfaceData surfaceData);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    @Mapping(target = "id", ignore = true)
    SurfaceData map(SurfaceDataCreateDto surfaceDataCreateDto);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    SurfaceData mapFull(SurfaceDataReadDto surfaceDataReadDto);
}

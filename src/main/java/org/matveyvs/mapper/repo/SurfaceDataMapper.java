package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.SurfaceDataCreateDto;
import org.matveyvs.dto.repo.SurfaceDataReadDto;
import org.matveyvs.entity.SurfaceData;

@Mapper(uses = {WellDataMapper.class})
public interface SurfaceDataMapper {
    @Mapping(target = "wellDataReadDto", source = "wellData")
    SurfaceDataReadDto map(SurfaceData surfaceData);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    @Mapping(target = "id", ignore = true)
    SurfaceData map(SurfaceDataCreateDto surfaceDataCreateDto);
    @Mapping(target = "wellData", source = "wellDataReadDto")
    SurfaceData mapFull(SurfaceDataReadDto surfaceDataReadDto);
}

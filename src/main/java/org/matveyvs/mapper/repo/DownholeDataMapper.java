package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.DownholeDataCreateDto;
import org.matveyvs.dto.repo.DownholeDataReadDto;
import org.matveyvs.entity.DownholeData;

@Mapper(uses = {WellDataMapper.class})
public interface DownholeDataMapper {
    @Mapping(target = "wellDataReadDto", source = "wellData")
    DownholeDataReadDto map(DownholeData downholeData);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wellData", source = "wellDataReadDto")
    DownholeData map(DownholeDataCreateDto downholeDataCreateDto);

    @Mapping(target = "wellData", source = "wellDataReadDto")
    DownholeData mapFull(DownholeDataReadDto downholeDataReadDto);
}

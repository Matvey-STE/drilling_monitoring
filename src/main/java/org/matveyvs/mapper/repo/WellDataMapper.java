package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.WellDataCreateDto;
import org.matveyvs.dto.repo.WellDataReadDto;
import org.matveyvs.entity.WellData;

@Mapper
public interface WellDataMapper {
    WellDataReadDto map(WellData wellData);

    @Mapping(target = "id", ignore = true)
    WellData map(WellDataCreateDto wellDataCreateDto);

    WellData mapFull(WellDataReadDto wellDataReadDto);
}

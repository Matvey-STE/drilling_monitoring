package org.matveyvs.mapper.repo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.repo.UserCreateDto;
import org.matveyvs.dto.repo.UserReadDto;
import org.matveyvs.entity.User;

@Mapper
public interface UserMapper {
    UserReadDto map(User user);
    @Mapping(target = "id", ignore = true)
    User map(UserCreateDto userCreateDto);

    User mapFull(UserReadDto userReadDto);

}

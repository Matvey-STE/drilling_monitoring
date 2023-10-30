package org.matveyvs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.entity.User;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    UserReadDto map(User user);
    @Mapping(target = "id", ignore = true)
    User map(UserCreateDto userCreateDto);

    User mapFull(UserReadDto userReadDto);

}

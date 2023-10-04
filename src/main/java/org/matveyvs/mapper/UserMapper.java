package org.matveyvs.mapper;

import org.matveyvs.dto.UserDto;
import org.matveyvs.entity.User;

public class UserMapper implements Mapper<UserDto, User>{
    private static final UserMapper INSTANCE = new UserMapper();
    @Override
    public UserDto mapFrom(User object) {
        return new UserDto(Math.toIntExact(object.getId()),
                object.getUserName(),
                object.getEmail(),
                object.getPassword(),
                object.getRole(),
                object.getCreatedAt(),
                object.getFirstName(),
                object.getLastName());
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}

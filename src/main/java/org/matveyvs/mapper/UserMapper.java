package org.matveyvs.mapper;

import org.matveyvs.dto.UserDto;
import org.matveyvs.entity.User;

public class UserMapper implements Mapper<UserDto, User>{
    private static final UserMapper INSTANCE = new UserMapper();
    @Override
    public UserDto mapFrom(User user) {
        return new UserDto(Math.toIntExact(user.getUserId()),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedAt(),
                user.getFirstName(),
                user.getLastName());
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}

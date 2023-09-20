package org.matveyvs.mapper;

import org.matveyvs.dto.UserDto;
import org.matveyvs.entity.User;

public class UserMapper implements Mapper<UserDto, User>{
    private static final UserMapper INSTANCE = new UserMapper();
    @Override
    public UserDto mapFrom(User user) {
        return new UserDto(Math.toIntExact(user.id()),
                user.userName(),
                user.email(),
                user.password(),
                user.role(),
                user.createdAt(),
                user.firstName(),
                user.lastName());
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }
}

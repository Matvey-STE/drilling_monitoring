package org.matveyvs.mapper;

import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.entity.Role;
import org.matveyvs.entity.User;

public class CreateUserMapper implements Mapper<User, CreateUserDto> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();
    @Override
    public User mapFrom(CreateUserDto object) {
        return new User(object.username(),
                object.email(),
                object.password(),
                Role.valueOf(object.role()),
                object.createdAt(),
                object.firstName(),
                object.lastName());
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
    private CreateUserMapper(){

    }
}

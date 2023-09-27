package org.matveyvs.mapper;

import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.entity.Role;
import org.matveyvs.entity.User;

public class CreateUserMapper implements Mapper<User, CreateUserDto> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();
    @Override
    public User mapFrom(CreateUserDto object) {
        return User.builder()
                .userName(object.username())
                .email(object.email())
                .password(object.password())
                .role(Role.valueOf(object.role()))
                .createdAt(object.createdAt())
                .firstName(object.firstName())
                .lastName(object.lastName())
                .build();
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }
    private CreateUserMapper(){

    }
}

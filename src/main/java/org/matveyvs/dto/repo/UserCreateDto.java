package org.matveyvs.dto.repo;

import org.matveyvs.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public record UserCreateDto(
        @NotNull
        String userName,
        @NotNull
        @Email
        String email,
        @NotNull
        String password,
        Role role,
        @NotNull
        Timestamp createdAt,
        String firstName,
        String lastName) {
}

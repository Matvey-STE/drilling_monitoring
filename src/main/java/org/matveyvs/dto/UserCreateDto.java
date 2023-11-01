package org.matveyvs.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.matveyvs.entity.Role;

import java.sql.Timestamp;

public record UserCreateDto(
        @NotNull
        String userName,

        @Email
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        Role role,
        @NotNull
        Timestamp createdAt,
        String firstName,
        String lastName) {
}

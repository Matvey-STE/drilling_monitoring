package org.matveyvs.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.matveyvs.entity.Role;

import java.sql.Timestamp;

public record UserCreateDto(
        @NotNull
        @Size(max = 3)
        String userName,
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

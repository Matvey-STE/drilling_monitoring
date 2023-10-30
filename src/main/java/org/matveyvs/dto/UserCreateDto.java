package org.matveyvs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.matveyvs.entity.Role;

import java.sql.Timestamp;
@Valid
public record UserCreateDto(
        @NotNull
        String userName,
        @NotNull
        String email,
        @NotNull
        String password,
        Role role,
        Timestamp createdAt,
        String firstName,
        String lastName) {
}

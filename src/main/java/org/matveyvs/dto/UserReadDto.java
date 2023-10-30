package org.matveyvs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.matveyvs.entity.Role;

import java.sql.Timestamp;
@Valid
public record UserReadDto(
        Integer id,
        @NotNull
        String userName,
        @Email
        @NotNull
        String email,
        @NotNull
        String password,
        Role role,
        @NotNull
        Timestamp createdAt,
        String firstName,
        String lastName) {
}

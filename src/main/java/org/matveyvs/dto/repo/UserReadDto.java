package org.matveyvs.dto.repo;

import org.matveyvs.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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

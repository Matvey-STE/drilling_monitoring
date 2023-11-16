package org.matveyvs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldNameConstants;
import org.matveyvs.entity.Role;

@Valid
@FieldNameConstants
public record UserCreateDto(
        @NotNull
        @NotEmpty(message = "The field Username shouldn't be empty")
        String username,
        @NotNull
        @Email(message = "Invalid Email")
        @NotEmpty(message = "The field Email shouldn't be empty")
        String email,
        @NotNull
        @NotEmpty(message = "The field Password shouldn't be empty")
        String password,
        @NotNull
        Role role,
        @NotEmpty(message = "The field First Name shouldn't be empty")
        String firstName,
        @NotEmpty(message = "The field Last Name shouldn't be empty")
        String lastName) {
}

package org.matveyvs.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record WellDataCreateDto(
        @NotNull
        @NotEmpty(message = "Company name shouldn't be empty")
        String companyName,
        @NotNull
        @NotEmpty(message = "Field name shouldn't be empty")
        String fieldName,
        String wellCluster,
        @NotNull
        @NotEmpty(message = "Well shouldn't be empty")
        String well) {
}

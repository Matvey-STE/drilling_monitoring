package org.matveyvs.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record WellDataReadDto(
        Integer id,
        @NotNull
        @NotEmpty
        String companyName,
        @NotNull
        @NotEmpty
        String fieldName,
        String wellCluster,
        @NotNull
        @NotEmpty
        String well) {
}

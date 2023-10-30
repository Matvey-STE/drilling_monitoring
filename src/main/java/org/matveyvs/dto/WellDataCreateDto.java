package org.matveyvs.dto;

import jakarta.validation.constraints.NotNull;

public record WellDataCreateDto(
        @NotNull
        String companyName,
        @NotNull
        String fieldName,
        String wellCluster,
        @NotNull
        String well) {
}

package org.matveyvs.dto.repo;

import javax.validation.constraints.NotNull;

public record WellDataCreateDto(
        @NotNull
        String companyName,
        @NotNull
        String fieldName,
        String wellCluster,
        @NotNull
        String well) {
}

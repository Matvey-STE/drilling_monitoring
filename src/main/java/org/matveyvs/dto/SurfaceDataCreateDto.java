package org.matveyvs.dto;


import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record SurfaceDataCreateDto(
        @NotNull
        Timestamp measuredDate,
        @NotNull
        Double measuredDepth,
        Double holeDepth, Double tvDepth, Double hookload,
        Double wob, Double blockPos, Double standpipePressure,
        WellDataReadDto wellDataReadDto) {
}

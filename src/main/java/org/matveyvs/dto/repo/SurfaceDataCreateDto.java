package org.matveyvs.dto.repo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public record SurfaceDataCreateDto(
        @NotNull
        Timestamp measuredDate,
        @NotNull
        Double measuredDepth,
        Double holeDepth, Double tvDepth, Double hookload,
        Double wob, Double blockPos, Double standpipePressure,
        @NotNull
        @Valid
        WellDataReadDto wellDataReadDto) {
}

package org.matveyvs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record GammaCreateDto(
        @NotNull
        Timestamp measureDate,
        @NotNull
        Double measuredDepth,
        Double grcx,
        @NotNull
        @Valid
        DownholeDataReadDto downholeDataReadDto) {

}

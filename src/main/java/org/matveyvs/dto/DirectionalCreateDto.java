package org.matveyvs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record DirectionalCreateDto(
        @NotNull
        Timestamp measureDate,
        @NotNull
        Double measuredDepth,
        Double gx, Double gy, Double gz, Double bx, Double by,
        Double bz, Double inc, Double azTrue, Double azMag,
        Double azCorr, Double toolfaceCorr,
        @NotNull
        @Valid
        DownholeDataReadDto downholeDataReadDto) {

}

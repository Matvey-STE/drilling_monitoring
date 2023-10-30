package org.matveyvs.dto;

import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record GammaReadDto(

        Integer id,
        @NotNull
        Timestamp measureDate,
        @NotNull
        Double measuredDepth,
        Double grcx,
        @NotNull
        DownholeDataReadDto downholeDataReadDto) {
}

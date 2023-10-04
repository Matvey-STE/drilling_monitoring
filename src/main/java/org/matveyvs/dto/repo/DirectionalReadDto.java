package org.matveyvs.dto.repo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public record DirectionalReadDto(
        Integer id,
        @NotNull
        Timestamp measureDate,
        @NotNull
        Double measuredDepth,
        Double gx, Double gy, Double gz, Double bx, Double by,
        Double bz, Double inc, Double azTrue, Double azMag,
        Double azCorr, Double toolfaceCorr,
        @Valid
        @NotNull
        DownholeDataReadDto downholeDataReadDto) {
}

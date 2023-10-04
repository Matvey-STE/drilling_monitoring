package org.matveyvs.dto.repo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

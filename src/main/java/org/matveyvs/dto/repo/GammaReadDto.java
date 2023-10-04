package org.matveyvs.dto.repo;

import javax.validation.constraints.NotNull;
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

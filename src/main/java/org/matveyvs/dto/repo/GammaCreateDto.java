package org.matveyvs.dto.repo;

import java.sql.Timestamp;

public record GammaCreateDto(Timestamp measureDate,
                             Double measuredDepth, Double grcx,
                             DownholeDataReadDto downholeDataReadDto) {

}

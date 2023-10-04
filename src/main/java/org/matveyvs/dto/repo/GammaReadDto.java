package org.matveyvs.dto.repo;

import java.sql.Timestamp;

public record GammaReadDto(Integer id, Timestamp measureDate,
                           Double measuredDepth, Double grcx,
                           DownholeDataReadDto downholeDataReadDto) {
}

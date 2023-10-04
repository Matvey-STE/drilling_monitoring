package org.matveyvs.dto.repo;

import java.sql.Timestamp;

public record DirectionalReadDto(Integer id, Timestamp measureDate, Double measuredDepth,
                                 Double gx, Double gy, Double gz, Double bx, Double by,
                                 Double bz, Double inc, Double azTrue, Double azMag,
                                 Double azCorr, Double toolfaceCorr,
                                 DownholeDataReadDto downholeDataReadDto) {
}

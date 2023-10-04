package org.matveyvs.dto.repo;

import java.sql.Timestamp;

public record SurfaceDataReadDto(Integer id, Timestamp measuredDate, Double measuredDepth,
                                 Double holeDepth, Double tvDepth, Double hookload,
                                 Double wob, Double blockPos, Double standpipePressure,
                                 WellDataReadDto wellDataReadDto) {
}

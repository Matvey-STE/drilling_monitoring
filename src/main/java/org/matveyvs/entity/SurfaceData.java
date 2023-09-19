package org.matveyvs.entity;

import java.sql.Timestamp;

public record SurfaceData(Long id, Timestamp measuredDate, Double measuredDepth, Double holeDepth,
                          Double tvDepth, Double hookload, Double wob, Double blockPos,
                          Double standpipePressure, WellData wellData) {
    public SurfaceData(Timestamp measuredDate, Double measuredDepth, Double holeDepth,
                       Double tvDepth, Double hookload, Double wob, Double blockPos,
                       Double standpipePressure, WellData wellData) {
        this(null, measuredDate, measuredDepth, holeDepth,
                tvDepth, hookload, wob, blockPos, standpipePressure, wellData);
    }
}

package org.matveyvs.entity;

import java.sql.Timestamp;

public record SurfaceData(Long id, Timestamp measuredDate, Double measuredDepth, Double holeDepth,
                          Double tvDepth, Double hookload, Double wob, Double blockPos,
                          Double standpipePressure) {
    public SurfaceData(Timestamp measuredDate, Double measuredDepth, Double holeDepth,
                       Double tvDepth, Double hookload, Double wob, Double blockPos,
                       Double standpipePressure) {
        this(null, measuredDate, measuredDepth, holeDepth, tvDepth, hookload, wob, blockPos, standpipePressure);
    }
}

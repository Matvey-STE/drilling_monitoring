package org.matveyvs.dto;

import java.sql.Timestamp;

public record SurfaceDataDto(Integer id, Timestamp measureDate, Double measureDepth,
                             Double holeDepth, Double tvDepth) {
}

package org.matveyvs.dto;

import java.sql.Timestamp;

public record SurfaceDataDto(Long id, Timestamp measureDate, Double measureDepth,
                             Double holeDepth, Double tvDepth) {
}

package org.matveyvs.dto;

import org.matveyvs.entity.DownholeData;

import java.sql.Timestamp;

public record DirectionalDto(Long id, Timestamp measureDate, Double measureDepth,
                             Double inc, Double azCorr, Double azTrue, DownholeData downholeData) {
}

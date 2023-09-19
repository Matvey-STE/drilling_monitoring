package org.matveyvs.dto;

import org.matveyvs.entity.DownholeData;

import java.sql.Timestamp;

public record GammaDto(Long id, Timestamp measureDate, Double measureDepth,
                       Double grcx, DownholeData downholeData) {
}

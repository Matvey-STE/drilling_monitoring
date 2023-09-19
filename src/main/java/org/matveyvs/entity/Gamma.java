package org.matveyvs.entity;

import java.sql.Timestamp;

public record Gamma(Long id, Timestamp timestamp, Double measuredDepth, Double grcx, DownholeData downholeData) {
    public Gamma(Timestamp timestamp, Double measuredDepth, Double grcx, DownholeData downholeData) {
        this(null, timestamp, measuredDepth, grcx, downholeData);
    }
}


package org.matveyvs.entity;

import java.sql.Timestamp;

public record Directional(Long id, Timestamp measureDate, Double measuredDepth, Double gx, Double gy, Double gz,
                          Double bx, Double by, Double bz, Double inc, Double azTrue, Double azMag, Double azCorr,
                          Double toolfaceCorr, DownholeData downholeData) {

    public Directional(Timestamp measureDate, Double measuredDepth, Double gx, Double gy, Double gz,
                       Double bx, Double by, Double bz, Double inc, Double azTrue, Double azMag,
                       Double azCorr, Double toolfaceCorr, DownholeData downholeData) {
        this(null, measureDate, measuredDepth, gx, gy, gz, bx, by, bz,
                inc, azTrue, azMag, azCorr, toolfaceCorr, downholeData);
    }
}

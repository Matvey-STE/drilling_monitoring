package org.matveyvs.entity;

public record Directional(Long id, Double measuredDepth, Double gx, Double gy, Double gz, Double bx, Double by,
                          Double bz, Double inc, Double azTrue, Double azMag, Double azCorr, Double toolfaceCorr) {

    public Directional(Double measuredDepth, Double gx, Double gy, Double gz, Double bx, Double by, Double bz,
                       Double inc, Double azTrue, Double azMag, Double azCorr, Double toolfaceCorr) {
        this(null, measuredDepth, gx, gy, gz, bx, by, bz, inc, azTrue, azMag, azCorr, toolfaceCorr);
    }
}

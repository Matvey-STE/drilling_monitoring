package org.matveyvs.entity;

public record Gamma(Long id, Double measuredDepth, Double grcx) {
    public Gamma(Double measuredDepth, Double grcx) {
        this(null, measuredDepth, grcx);
    }
}

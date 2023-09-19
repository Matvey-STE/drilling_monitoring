package org.matveyvs.entity;

public record DownholeData(Long id, WellData wellData) {
    public DownholeData(WellData wellData) {
        this(null, wellData);
    }

}

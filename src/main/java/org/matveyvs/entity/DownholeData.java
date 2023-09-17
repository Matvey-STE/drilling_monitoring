package org.matveyvs.entity;

import java.sql.Timestamp;

public record DownholeData(Long id, Timestamp measuredDate, Directional directional,
                           Gamma gamma) {
    public DownholeData(Timestamp measuredDate, Directional directional,
                        Gamma gamma) {
        this(null, measuredDate, directional, gamma);
    }

}

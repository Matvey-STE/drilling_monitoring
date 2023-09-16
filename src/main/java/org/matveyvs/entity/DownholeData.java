package org.matveyvs.entity;

import java.sql.Timestamp;

public record DownholeData (Long id, Timestamp measureDate, Directional directional,
                            Gamma gamma){
    public DownholeData(Timestamp measureDate, Directional directional,
                        Gamma gamma){
        this(null,measureDate,directional,gamma);
    }

}

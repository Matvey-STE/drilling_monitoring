package org.matveyvs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SurfaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "measure_date")
    Timestamp measuredDate;
    @Column(name = "mdepth")
    Double measuredDepth;
    Double holeDepth;
    @Column(name = "tvdepth")
    Double tvDepth;
    Double hookload;
    Double wob;
    Double blockPos;
    @Column(name = "standpipe_pr")
    Double standpipePressure;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "welldata_id")
    WellData wellData;

}

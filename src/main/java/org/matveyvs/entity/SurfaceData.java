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
@Table(name = "surface_data")
public class SurfaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "measure_date")
    Timestamp measuredDate;
    @Column(name = "mdepth")
    Double measuredDepth;
    @Column (name = "hole_depth")
    Double holeDepth;
    @Column(name = "tvdepth")
    Double tvDepth;
    Double hookload;
    Double wob;
    @Column (name = "bleck_pos")
    Double blockPos;
    @Column(name = "standpipe_pr")
    Double standpipePressure;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "welldata_id")
    WellData wellData;

}

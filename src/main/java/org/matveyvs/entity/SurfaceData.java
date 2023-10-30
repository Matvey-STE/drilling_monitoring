package org.matveyvs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NamedEntityGraph(
        name = "WellDataFromSurface",
        attributeNodes = {
                @NamedAttributeNode("wellData")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "wellData")
@EqualsAndHashCode(exclude = "wellData")
@Builder
@Entity
@Table(name = "surface_data")
public class SurfaceData implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "measure_date")
    private Timestamp measuredDate;
    @Column(name = "mdepth")
    private Double measuredDepth;
    @Column(name = "hole_depth")
    private Double holeDepth;
    @Column(name = "tvdepth")
    private Double tvDepth;
    private Double hookload;
    private Double wob;
    @Column(name = "bleck_pos")
    private Double blockPos;
    @Column(name = "standpipe_pr")
    private Double standpipePressure;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welldata_id")
    private WellData wellData;

    public void setWellData(WellData wellData) {
        this.wellData = wellData;
        this.wellData.getSurfaceDataList().add(this);
    }
}

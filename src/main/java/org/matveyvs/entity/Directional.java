package org.matveyvs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import java.sql.Timestamp;

@FetchProfile(name = "withDownloadsDirectional", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = Directional.class, association = "downholeData", mode = FetchMode.JOIN),
        @FetchProfile.FetchOverride(entity = DownholeData.class, association = "wellData", mode = FetchMode.JOIN),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Directional implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "measure_date")
    private Timestamp measureDate;
    @Column(name = "mdepth")
    private Double measuredDepth;
    private Double gx;
    private Double gy;
    private Double gz;
    private Double bx;
    private Double by;
    private Double bz;
    private Double inc;
    @Column(name = "az_true")
    private Double azTrue;
    @Column(name = "az_mag")
    private Double azMag;
    @Column(name = "az_corr")
    private Double azCorr;
    @Column(name = "toolface_corr")
    private Double toolfaceCorr;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "downhole_id")
    private DownholeData downholeData;
}

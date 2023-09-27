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
public class Directional{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    private Double azTrue;
    private Double azMag;
    private Double azCorr;
    private Double toolfaceCorr;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "downhole_id")
    private DownholeData downholeData;
}

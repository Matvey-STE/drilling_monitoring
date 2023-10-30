package org.matveyvs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@NamedEntityGraph(
        name = "DownholeDataAndWellDataFromDirectional",
        attributeNodes = {
                @NamedAttributeNode("downholeData"),
                @NamedAttributeNode(value = "downholeData", subgraph = "wellData"),
        },
        subgraphs = {
                @NamedSubgraph(name = "wellData",attributeNodes = @NamedAttributeNode("wellData"))
        }
        )
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"downholeData"})
@EqualsAndHashCode(exclude = {"downholeData"})
@Builder
@Entity
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downhole_id")
    private DownholeData downholeData;
    public void setDownholeData(DownholeData downholeData) {
        this.downholeData = downholeData;
        this.downholeData.getDirectionalList().add(this);
    }
}

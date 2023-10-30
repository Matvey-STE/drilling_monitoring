package org.matveyvs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@NamedEntityGraph(
        name = "DownholeDataAndWellDataFromGamma",
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
public class Gamma implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "measure_date")
    private Timestamp measureDate;
    @Column(name = "mdepth")
    private Double measuredDepth;
    private Double grcx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "downhole_id")
    private DownholeData downholeData;
    public void setDownholeData(DownholeData downholeData) {
        this.downholeData = downholeData;
        this.downholeData.getGammaList().add(this);
    }
}


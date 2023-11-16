package org.matveyvs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(
        name = "WellDataFromDownhole",
        attributeNodes = {
                @NamedAttributeNode("wellData"),
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"wellData", "directionalList", "gammaList"})
@EqualsAndHashCode(exclude = {"wellData", "directionalList", "gammaList"})
@Builder
@Entity
@Table(name = "downhole_data")
public class DownholeData implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Builder.Default
    @OneToMany(mappedBy = "downholeData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Directional> directionalList = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "downholeData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Gamma> gammaList = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welldata_id")
    private WellData wellData;

    public void addDirectional(Directional directional){
        directionalList.add(directional);
        directional.setDownholeData(this);
    }
    public void addGamma(Gamma gamma){
        gammaList.add(gamma);
        gamma.setDownholeData(this);
    }

    public void setWellData(WellData wellData) {
        this.wellData = wellData;
        this.wellData.getDownholeDataList().add(this);
    }
}

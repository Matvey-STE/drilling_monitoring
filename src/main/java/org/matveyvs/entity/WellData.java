package org.matveyvs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "SurfaceAndDownholeListsFromWellData",
        attributeNodes = {
                @NamedAttributeNode("surfaceDataList"),
                @NamedAttributeNode("downholeDataList")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"surfaceDataList","downholeDataList"})
@EqualsAndHashCode(exclude = {"surfaceDataList","downholeDataList"})
@Builder
@Entity
@Table(name = "well_data")
public class WellData implements BaseEntity<Integer>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "well_cluster")
    private String wellCluster;
    private String well;
    @Builder.Default
    @OneToMany(mappedBy = "wellData",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //set to prevent MultipleBagFetchException
    private Set<SurfaceData> surfaceDataList = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "wellData",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DownholeData> downholeDataList = new ArrayList<>();

    public void addSurfaceData(SurfaceData surfaceData){
        surfaceDataList.add(surfaceData);
        surfaceData.setWellData(this);
    }
    public void addDownholeData(DownholeData downholeData){
        downholeDataList.add(downholeData);
        downholeData.setWellData(this);
    }
}

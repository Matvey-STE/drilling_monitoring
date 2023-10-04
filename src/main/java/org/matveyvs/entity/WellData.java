package org.matveyvs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "well_data")
//@Audited
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "WellData")
public class WellData implements BaseEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "company_name")
    String companyName;
    @Column(name = "field_name")
    String fieldName;
    @Column(name = "well_cluster")
    String wellCluster;
    String well;
}

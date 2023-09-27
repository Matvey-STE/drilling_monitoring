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
public class WellData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String companyName;
    String fieldName;
    String wellCluster;
    String well;
}

package org.matveyvs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import java.sql.Timestamp;
@FetchProfile(name = "withDownloadsGamma", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = Gamma.class, association = "downholeData", mode = FetchMode.JOIN),
        @FetchProfile.FetchOverride(entity = DownholeData.class, association = "wellData", mode = FetchMode.JOIN),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Gamma{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name = "measure_date")
    Timestamp measureDate;
    @Column(name = "mdepth")
    Double measuredDepth;
    Double grcx;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "downhole_id")
    DownholeData downholeData;
}


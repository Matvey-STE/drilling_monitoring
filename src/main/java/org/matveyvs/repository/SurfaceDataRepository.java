package org.matveyvs.repository;

import org.matveyvs.entity.SurfaceData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurfaceDataRepository extends JpaRepository<SurfaceData, Integer> {
    @EntityGraph(value = "WellDataFromSurface")
    List<SurfaceData> findAll();

    @EntityGraph(value = "WellDataFromSurface")
    List<SurfaceData> findAllByWellDataId(Integer id);
}

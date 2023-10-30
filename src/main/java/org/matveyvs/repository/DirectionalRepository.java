package org.matveyvs.repository;

import org.matveyvs.entity.Directional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectionalRepository extends JpaRepository<Directional, Integer> {
    @EntityGraph(value = "DownholeDataAndWellDataFromDirectional")
    List<Directional> findAll();

    @EntityGraph(value = "DownholeDataAndWellDataFromDirectional")
    List<Directional> findAllByDownholeDataId(Integer id);

    @EntityGraph(value = "DownholeDataAndWellDataFromDirectional")
    Optional<Directional> findById(Integer id);
}

package org.matveyvs.repository;

import org.matveyvs.entity.Gamma;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GammaRepository extends JpaRepository<Gamma, Integer> {
    @EntityGraph(value = "DownholeDataAndWellDataFromGamma")
    List<Gamma> findAll();

    @EntityGraph(value = "DownholeDataAndWellDataFromGamma")
    List<Gamma> findAllByDownholeDataId(Integer id);

    @EntityGraph(value = "DownholeDataAndWellDataFromGamma")
    Optional<Gamma> findById(Integer id);
}

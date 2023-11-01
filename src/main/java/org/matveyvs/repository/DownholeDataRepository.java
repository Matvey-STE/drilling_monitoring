package org.matveyvs.repository;

import org.matveyvs.entity.DownholeData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DownholeDataRepository extends JpaRepository<DownholeData, Integer> {

    @EntityGraph(value = "WellDataFromDownhole")
    @Override
    List<DownholeData> findAll();

    @EntityGraph(value = "WellDataFromDownhole")
    List<DownholeData> findAllByWellDataId(Integer id);

    @EntityGraph(value = "WellDataFromDownhole")
    @Override
    Optional<DownholeData> findById(Integer id);
}

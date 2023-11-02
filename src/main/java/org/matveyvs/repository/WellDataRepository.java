package org.matveyvs.repository;

import org.matveyvs.entity.WellData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WellDataRepository extends JpaRepository<WellData, Integer> {
    Page<WellData> getAllBy(Pageable pageable);
}

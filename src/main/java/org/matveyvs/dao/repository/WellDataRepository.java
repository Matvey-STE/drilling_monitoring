package org.matveyvs.dao.repository;

import org.matveyvs.entity.WellData;

import javax.persistence.EntityManager;

public class WellDataRepository extends BaseRepository<Integer, WellData>{
    public WellDataRepository(EntityManager entityManager) {
        super(WellData.class, entityManager);
    }
}

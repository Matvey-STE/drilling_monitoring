package org.matveyvs.dao.repository;

import org.hibernate.HibernateException;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.exception.DaoException;

import javax.persistence.EntityManager;
import java.util.List;

public class SurfaceDataRepository extends BaseRepository<Integer, SurfaceData> {
    private final EntityManager entityManager;

    public SurfaceDataRepository(EntityManager entityManager) {
        super(SurfaceData.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<SurfaceData> findAllByWelldataId(Integer welldataId) {
        List<SurfaceData> surfaceDataList;
        try {
            surfaceDataList = entityManager
                    .createQuery("""
                            select s
                            from SurfaceData s
                            join fetch s.wellData w
                            where w.id = :id
                            """, SurfaceData.class)
                    .setParameter("id", welldataId).getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
        return surfaceDataList;
    }
}

package org.matveyvs.dao.repository;

import org.hibernate.HibernateException;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.exception.DaoException;

import javax.persistence.EntityManager;
import java.util.List;

public class DownholeDataRepository extends BaseRepository<Integer, DownholeData> {
    private final EntityManager entityManager;

    public DownholeDataRepository(EntityManager entityManager) {
        super(DownholeData.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<DownholeData> findAllByWellId(Integer welldataId) {
        List<DownholeData> downholeDataList;
        try {
            downholeDataList = entityManager
                    .createQuery("""
                            select d
                            from DownholeData d
                            join fetch d.wellData w
                            where w.id = :id
                            """, DownholeData.class)
                    .setParameter("id", welldataId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
        return downholeDataList;
    }
}

package org.matveyvs.dao.repository;

import org.hibernate.HibernateException;
import org.matveyvs.entity.Directional;
import org.matveyvs.exception.DaoException;

import javax.persistence.EntityManager;
import java.util.List;

public class DirectionalRepository extends BaseRepository<Integer, Directional> {
    private final EntityManager entityManager;

    public DirectionalRepository(EntityManager entityManager) {
        super(Directional.class, entityManager);
        this.entityManager = entityManager;
    }
    public List<Directional> findAllByDownholeId(Integer downholeId) {
        List<Directional> directionals;
        try {
            directionals = entityManager
                    .createQuery("""
                            select d
                            from Directional d
                            join fetch d.downholeData h
                            join fetch h.wellData
                            where h.id = :id
                            """, Directional.class)
                    .setParameter("id", downholeId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
        return directionals;
    }
}

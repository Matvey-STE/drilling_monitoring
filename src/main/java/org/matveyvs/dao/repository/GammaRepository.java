package org.matveyvs.dao.repository;

import org.hibernate.HibernateException;
import org.matveyvs.entity.Gamma;
import org.matveyvs.exception.DaoException;

import javax.persistence.EntityManager;
import java.util.List;

public class GammaRepository extends BaseRepository<Integer, Gamma> {
    private final EntityManager entityManager;

    public GammaRepository(EntityManager entityManager) {
        super(Gamma.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<Gamma> findAllByDownholeId(Integer downholeId) {
        List<Gamma> gammas;
        try {
            gammas = entityManager
                    .createQuery("""
                            select g
                            from Gamma g
                            join fetch g.downholeData h
                            join fetch h.wellData
                            where h.id = :id
                            """, Gamma.class)
                    .setParameter("id", downholeId)
                    .getResultList();
        } catch (HibernateException e) {
            throw new DaoException(e);
        }
        return gammas;
    }
}

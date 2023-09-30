package org.matveyvs.dao;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.matveyvs.entity.Gamma;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static org.matveyvs.entity.QDownholeData.downholeData;
import static org.matveyvs.entity.QGamma.*;
import static org.matveyvs.entity.QWellData.wellData;

@Slf4j
public class GammaDao implements Dao<Integer, Gamma> {
    private static final GammaDao INSTANCE = new GammaDao();

    @Override
    public Gamma save(Gamma gamma) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(gamma);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", gamma);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return gamma;
    }

    @Override
    public List<Gamma> findAll() {
        List<Gamma> gammas;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            gammas = session
                    .createQuery("""
                            select g
                            from Gamma g
                            join fetch g.downholeData h
                            join fetch h.wellData
                            """, Gamma.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", gammas.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return gammas;
    }

    public List<Gamma> findAllByDownholeId(Integer downholeId) {
        List<Gamma> gammas;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            gammas = session
                    .createQuery("""
                            select g
                            from Gamma g
                            join fetch g.downholeData h
                            join fetch h.wellData
                            where h.id = :id
                            """, Gamma.class)
                    .setParameter("id", downholeId)
                    .list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", gammas.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return gammas;
    }

    @Override
    public Optional<Gamma> findById(Integer id) {
        Gamma gamma = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.enableFetchProfile("withDownloadsGamma");
            session.beginTransaction();
            Query query = session
                    .createQuery("""
                            SELECT g
                            FROM Gamma g
                            join fetch g.downholeData h
                            join fetch h.wellData
                            WHERE g.id = :id
                            """, Gamma.class)
                    .setParameter("id", id);
            gamma = (Gamma) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", gamma);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown ", e);
        }
        return Optional.ofNullable(gamma);
    }

    @Override
    public boolean update(Gamma gamma) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(gamma);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", gamma);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Gamma gamma = session.get(Gamma.class, id);
            session.delete(gamma);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", gamma);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
    }

    public List<Gamma> findAllGammaByFieldName(String fieldName) {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.enableFetchProfile("withDownloadsGamma");
        return new JPAQuery<Gamma>(session)
                .select(gamma)
                .from(gamma)
                .join(gamma.downholeData, downholeData)
                .join(downholeData.wellData, wellData)
                .where(wellData.fieldName.eq(fieldName))
                .fetch();
    }

    public List<Gamma> findAllGamByDepthBetweenAndFieldName
            (String fieldName, Double startDepth, Double endDepth) {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.enableFetchProfile("withDownloadsGamma");
        return new JPAQuery<Gamma>(session)
                .select(gamma)
                .from(gamma)
                .join(gamma.downholeData, downholeData)
                .join(downholeData.wellData, wellData)
                .where(gamma.measuredDepth.between(startDepth, endDepth)
                        .and(wellData.fieldName.eq(fieldName)))
                .fetch();
    }

    private GammaDao() {

    }

    public static GammaDao getInstance() {
        return GammaDao.INSTANCE;
    }
}

package org.matveyvs.dao;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.matveyvs.entity.Directional;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static org.matveyvs.entity.QDirectional.*;
import static org.matveyvs.entity.QDownholeData.*;
import static org.matveyvs.entity.QWellData.wellData;

@Slf4j
public class DirectionalDao implements Dao<Integer, Directional> {
    private static final DirectionalDao INSTANCE = new DirectionalDao();

    @Override
    public Directional save(Directional directional) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(directional);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", directional);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return directional;
    }

    @Override
    public List<Directional> findAll() {
        List<Directional> directionals;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            directionals = session
                    .createQuery("""
                            select d
                            from Directional d
                            join fetch d.downholeData h
                            join fetch h.wellData
                            """, Directional.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", directionals.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return directionals;
    }

    public List<Directional> findAllByDownholeId(Integer downholeId) {
        List<Directional> directionals;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            directionals = session
                    .createQuery("""
                            select d
                            from Directional d
                            join fetch d.downholeData h
                            join fetch h.wellData
                            where h.id = :id
                            """, Directional.class)
                    .setParameter("id", downholeId)
                    .list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", directionals.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return directionals;
    }

    @Override
    public Optional<Directional> findById(Integer id) {
        Directional directional = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session
                    .createQuery("""
                            SELECT d
                            FROM Directional d
                            join fetch d.downholeData h
                            join fetch h.wellData
                            WHERE d.id = :id
                            """, Directional.class)
                    .setParameter("id", id);
            directional = (Directional) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", directional);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown ", e);
        }
        return Optional.ofNullable(directional);
    }

    @Override
    public boolean update(Directional directional) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(directional);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", directional);
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
            Directional directional = session.get(Directional.class, id);
            session.delete(directional);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", directional);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
    }

    public List<Directional> findAllDirectionalByFieldName(String fieldName) {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.enableFetchProfile("withDownloadsDirectional");
        return new JPAQuery<Directional>(session)
                .select(directional)
                .from(directional)
                .join(directional.downholeData, downholeData)
                .join(downholeData.wellData, wellData)
                .where(wellData.fieldName.eq(fieldName))
                .fetch();
    }

    public List<Directional> findAllDirByDepthBetweenAndFieldName
            (String fieldName, Double startDepth, Double endDepth) {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.enableFetchProfile("withDownloadsDirectional");
        return new JPAQuery<Directional>(session)
                .select(directional)
                .from(directional)
                .join(directional.downholeData, downholeData)
                .join(downholeData.wellData, wellData)
                .where(directional.measuredDepth.between(startDepth, endDepth)
                        .and(wellData.fieldName.eq(fieldName)))
                .fetch();
    }

    private DirectionalDao() {

    }

    public static DirectionalDao getInstance() {
        return DirectionalDao.INSTANCE;
    }
}

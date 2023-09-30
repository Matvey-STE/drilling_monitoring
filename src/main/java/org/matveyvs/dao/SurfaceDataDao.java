package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.matveyvs.entity.SurfaceData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SurfaceDataDao implements Dao<Integer, SurfaceData> {
    private static final SurfaceDataDao INSTANCE = new SurfaceDataDao();

    @Override
    public SurfaceData save(SurfaceData surfaceData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(surfaceData);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", surfaceData);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return surfaceData;
    }

    @Override
    public List<SurfaceData> findAll() {
        List<SurfaceData> surfaceDataList;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            surfaceDataList = session
                    .createQuery("""
                            select s
                            from SurfaceData s
                            join fetch s.wellData
                            """, SurfaceData.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", surfaceDataList.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return surfaceDataList;
    }

    public List<SurfaceData> findAllByWelldataId(Integer welldataId) {
        List<SurfaceData> surfaceDataList;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            surfaceDataList = session
                    .createQuery("""
                            select s
                            from SurfaceData s
                            join fetch s.wellData w
                            where w.id = :id
                            """, SurfaceData.class)
                    .setParameter("id", welldataId)
                    .list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", surfaceDataList.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return surfaceDataList;
    }

    @Override
    public Optional<SurfaceData> findById(Integer id) {
        SurfaceData surfaceData = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session
                    .createQuery("""
                            SELECT s
                            FROM SurfaceData s
                            join fetch s.wellData
                            WHERE s.id = :id
                            """, SurfaceData.class)
                    .setParameter("id", id);
            surfaceData = (SurfaceData) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", surfaceData);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown ", e);
        }
        return Optional.ofNullable(surfaceData);
    }

    @Override
    public boolean update(SurfaceData surfaceData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(surfaceData);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", surfaceData);
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
            SurfaceData surfaceData = session.get(SurfaceData.class, id);
            session.delete(surfaceData);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", surfaceData);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
    }

    private SurfaceDataDao() {

    }

    public static SurfaceDataDao getInstance() {
        return SurfaceDataDao.INSTANCE;
    }
}

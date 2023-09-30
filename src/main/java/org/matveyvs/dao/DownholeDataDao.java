package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.matveyvs.entity.DownholeData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DownholeDataDao implements Dao<Integer, DownholeData> {
    private static final DownholeDataDao INSTANCE = new DownholeDataDao();

    @Override
    public DownholeData save(DownholeData downholeData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(downholeData);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", downholeData);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return downholeData;
    }

    @Override
    public List<DownholeData> findAll() {
        List<DownholeData> downholeDataList;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            downholeDataList = session
                    .createQuery("""
                            select d
                            from DownholeData d
                            join fetch d.wellData
                            """, DownholeData.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", downholeDataList.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return downholeDataList;
    }

    public List<DownholeData> findAllByWellId(Integer welldataId) {
        List<DownholeData> downholeDataList;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            downholeDataList = session
                    .createQuery("""
                            select d
                            from DownholeData d
                            join fetch d.wellData w
                            where w.id = :id
                            """, DownholeData.class)
                    .setParameter("id", welldataId)
                    .list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", downholeDataList.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
        return downholeDataList;
    }

    @Override
    public Optional<DownholeData> findById(Integer id) {
        DownholeData downholeData = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session
                    .createQuery("""
                            SELECT d
                            FROM DownholeData d
                            join fetch d.wellData
                            WHERE d.id = :id
                            """, DownholeData.class)
                    .setParameter("id", id);
            downholeData = (DownholeData) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", downholeData);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown ", e);
        }
        return Optional.ofNullable(downholeData);
    }

    @Override
    public boolean update(DownholeData downholeData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(downholeData);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", downholeData);
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
            DownholeData downholeData = session.get(DownholeData.class, id);
            session.delete(downholeData);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", downholeData);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown ", e);
            throw new DaoException(e);
        }
    }

    private DownholeDataDao() {

    }

    public static DownholeDataDao getInstance() {
        return DownholeDataDao.INSTANCE;
    }
}

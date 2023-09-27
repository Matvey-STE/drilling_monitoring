package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.matveyvs.entity.WellData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Slf4j
public class WellDataDao implements Dao<Integer, WellData> {
    private static final WellDataDao INSTANCE = new WellDataDao();

    @Override
    public WellData save(WellData wellData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(wellData);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", wellData);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return wellData;
    }

    @Override
    public List<WellData> findAll() {
        List<WellData> wellDataList;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            wellDataList = session
                    .createQuery("select w from WellData w", WellData.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", wellDataList.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e);
            throw new DaoException(e);
        }
        return wellDataList;
    }

    @Override
    public Optional<WellData> findById(Integer id) {
        WellData wellData = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session
                    .createQuery("SELECT w FROM WellData w WHERE id = :id", WellData.class);
            query.setParameter("id", id);
            wellData = (WellData) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", wellData);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown {}", e);
        }
        return Optional.ofNullable(wellData);
    }

    @Override
    public boolean update(WellData wellData) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(wellData);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", wellData);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e);
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            WellData wellData = session.get(WellData.class, id);
            session.delete(wellData);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", wellData);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e);
            throw new DaoException(e);
        }
    }

    private WellDataDao() {

    }

    public static WellDataDao getInstance() {
        return WellDataDao.INSTANCE;
    }
}

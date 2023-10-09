package org.matveyvs.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.entity.User;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDao implements Dao<Integer, User> {
    private static final UserDao INSTANCE = new UserDao();

    @Override
    public User save(User user) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            log.info("The entity {} was saved in database", user);
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e.getMessage(), e);
            throw new DaoException(e);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            users = session
                    .createQuery("select u from User u", User.class).list();
            session.getTransaction().commit();
            log.info("The entities size of {} was found in database", users.size());
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e);
            throw new DaoException(e);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) {
        User user = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session
                    .createQuery("SELECT u FROM User u WHERE id = :id", User.class);
            query.setParameter("id", id);
            user = (User) query.getSingleResult();
            session.getTransaction().commit();
            log.info("The entity {} was found in database", user);
        } catch (HibernateException e) {
            e.printStackTrace();
            log.error("An exception was thrown {}", e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean update(User user) {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            log.info("The entity {} was updated in database", user);
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
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
            log.info("The entity {} was deleted from database", user);
            return true;
        } catch (HibernateException e) {
            log.error("An exception was thrown {}", e);
            throw new DaoException(e);
        }
    }

    public Optional<User> findByFilter(UserDaoFilter userDaoFilter) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            String hql = "select u FROM User u WHERE 1 = 1";

            if (userDaoFilter.username() != null) {
                hql += " AND u.userName = :username";
            }
            if (userDaoFilter.email() != null) {
                hql += " AND u.email = :email";
            }
            if (userDaoFilter.password() != null) {
                hql += " AND u.password = :password";
            }

            TypedQuery<User> query = session.createQuery(hql, User.class);

            if (userDaoFilter.username() != null) {
                query.setParameter("username", userDaoFilter.username());
            }
            if (userDaoFilter.email() != null) {
                query.setParameter("email", userDaoFilter.email());
            }
            if (userDaoFilter.password() != null) {
                query.setParameter("password", userDaoFilter.password());
            }

            List<User> results = query.getResultList();

            if (!results.isEmpty()) {
                return Optional.of(results.get(0));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private UserDao() {

    }

    public static UserDao getInstance() {
        return UserDao.INSTANCE;
    }
}

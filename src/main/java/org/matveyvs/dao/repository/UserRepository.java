package org.matveyvs.dao.repository;

import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.entity.User;
import org.matveyvs.exception.DaoException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<Integer, User> {
    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }

    public Optional<User> findByFilter(UserDaoFilter userDaoFilter) {
        try {
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
            if(userDaoFilter.password() == null &&
               userDaoFilter.email() == null &&
               userDaoFilter.username() == null){
                hql += " AND 1 = 0";
            }

            TypedQuery<User> query = entityManager.createQuery(hql, User.class);

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
}

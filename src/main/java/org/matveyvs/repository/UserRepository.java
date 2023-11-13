package org.matveyvs.repository;

import org.matveyvs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer> {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmailOrUsername(String email, String username);

    @Query(value = "select u from User u where " +
                   "concat(u.username, u.email, u.password, u.role, u.firstName, u.lastName) like %?1%")
    List<User> findByKeyWord(String keyword);

    @Query(value = "select u from User u where " +
                   "concat(u.username, u.email, u.password, u.role, u.firstName, u.lastName) like %?1%")
    Page<User> findAllBy(String keyword, Pageable pageable);
}

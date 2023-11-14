package org.matveyvs.service.integration.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.entity.User;
import org.matveyvs.repository.UserRepository;
import org.matveyvs.service.config.annotation.IT;
import org.matveyvs.utils.RandomWellDataBaseCreator;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@IT
@AllArgsConstructor
class UserRepositoryTest {
    private RandomWellDataBaseCreator randomWellDataBaseCreator;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        List<User> all = userRepository.findAll();
        if (all.isEmpty()) {
            randomWellDataBaseCreator.createRandomDataForTests();
        }
    }

    @Test
    void findByEmailAndPassword() {
        var user = User.builder()
                .username("repo test username")
                .email("repo test email")
                .password("repo test password")
                .build();
        User save = userRepository.save(user);
        Optional<User> byEmailAndPassword = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertTrue("find by email and password test present", byEmailAndPassword.isPresent());
        assertEquals("find by email and password test email and password",
                byEmailAndPassword.get().getEmail(), user.getEmail());
        userRepository.delete(save);
    }

    @Test
    void findByEmailOrUserName() {
        var user = User.builder()
                .username("repo test username")
                .email("repo test email")
                .password("repo test password")
                .build();
        User save = userRepository.save(user);
        Optional<User> byEmailOrUserName = userRepository.findByEmailOrUsername(save.getEmail(), save.getUsername());
        assertTrue("find by email or username test present", byEmailOrUserName.isPresent());
        assertEquals("find by email and password test email",
                byEmailOrUserName.get().getEmail(), user.getEmail());
        assertEquals("find by email and password test username",
                byEmailOrUserName.get().getUsername(), user.getUsername());
        userRepository.delete(user);
    }
}
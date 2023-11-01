package org.matveyvs.service.mockito.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
import org.matveyvs.entity.User;
import org.matveyvs.mapper.UserMapper;
import org.matveyvs.mapper.UserMapperImpl;
import org.matveyvs.repository.UserRepository;
import org.matveyvs.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void UserServiceRegistrationTest() {
        var userCreateDtoValidationTest = new UserCreateDto(
                null,
                "email@email.ru",
                "password",
                Role.USER,
                Timestamp.valueOf(LocalDateTime.now()),
                "name",
                "last name");
        Set<ConstraintViolation<UserCreateDto>> set = validator.validate(userCreateDtoValidationTest);
        assertFalse(set.isEmpty(), "constraints validation test fail");

        var userCreateDto = new UserCreateDto(
                "username",
                "email@email.ru",
                "password",
                Role.USER,
                Timestamp.valueOf(LocalDateTime.now()),
                "name",
                "last name");
        Set<ConstraintViolation<UserCreateDto>> validate = validator.validate(userCreateDto);
        assertTrue(validate.isEmpty());

        User map = userMapper.map(userCreateDto);
        assertNotNull(map, "user mapper fail test");
        map.setId(1);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(map);

        Integer integer = userService.create(userCreateDto);

        assertEquals(integer, map.getId(), "user service fail test");
    }
}

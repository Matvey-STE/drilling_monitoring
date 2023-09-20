package org.matveyvs.service;

import org.matveyvs.dao.UserDao;
import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.dto.UserDto;
import org.matveyvs.entity.User;
import org.matveyvs.exception.ValidationException;
import org.matveyvs.mapper.CreateUserMapper;
import org.matveyvs.mapper.UserMapper;
import org.matveyvs.validator.CreateUserValidator;

import java.util.Optional;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final CreateUserMapper mapper = CreateUserMapper.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserValidator validator = CreateUserValidator.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();
    public Integer create(CreateUserDto createUserDto){
        var validationResult = validator.isValid(createUserDto);
        if(!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }

        var user = mapper.mapFrom(createUserDto);
        User userResult = userDao.save(user);
        return Math.toIntExact(userResult.id());
    }

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password).map(userMapper::mapFrom);
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    private UserService(){

    }
}

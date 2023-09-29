package org.matveyvs.service;

import org.matveyvs.dao.UserDao;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.dto.UserDto;
import org.matveyvs.entity.User;
import org.matveyvs.exception.ValidationException;
import org.matveyvs.mapper.CreateUserMapper;
import org.matveyvs.mapper.UserMapper;
import org.matveyvs.validator.CreateUserValidator;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return Math.toIntExact(userResult.getUserId());
    }

    public Optional<UserDto> login(String email, String password) {
        String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern emailPattern = Pattern.compile(pattern);
        Matcher matcher = emailPattern.matcher(email);
        UserDaoFilter userDaoFilter;
        if (matcher.matches()){
            userDaoFilter = new UserDaoFilter(null,email,password);
        } else {
            userDaoFilter = new UserDaoFilter(email, null, password);
        }
        return userDao.findByFilter(userDaoFilter).map(userMapper::mapFrom);

    }

    public boolean checkIfExist(String username, String email) {
        var usernameFilter = new UserDaoFilter(username,null,null);
        var emailFilter = new UserDaoFilter(null, email,null);
        return userDao.findByFilter(usernameFilter).isPresent() ||
               userDao.findByFilter(emailFilter).isPresent();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    private UserService(){

    }
}

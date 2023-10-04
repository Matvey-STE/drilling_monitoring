package org.matveyvs.service.repo.service;

import lombok.RequiredArgsConstructor;
import org.matveyvs.dao.filter.UserDaoFilter;
import org.matveyvs.dao.repository.UserRepository;
import org.matveyvs.dto.repo.UserCreateDto;
import org.matveyvs.dto.repo.UserReadDto;
import org.matveyvs.entity.User;
import org.matveyvs.mapper.repo.UserMapperImpl;


import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public Integer create(UserCreateDto userCreateDto) {
        var validator = validatorFactory.getValidator();
        var  validateResult = validator.validate(userCreateDto);
        if (!validateResult.isEmpty()){
            throw new ConstraintViolationException(validateResult);
        }
        var entity = userMapper.map(userCreateDto);
        return userRepository.save(entity).getId();
    }

    public boolean update(UserReadDto userReadDto) {
        var validator = validatorFactory.getValidator();
        var  validateResult = validator.validate(userReadDto);
        if (!validateResult.isEmpty()){
            throw new ConstraintViolationException(validateResult);
        }
        var optional = userRepository.findById(userReadDto.id());
        if (optional.isPresent()) {
            User user = userMapper.mapFull(userReadDto);
            userRepository.update(user);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserReadDto> findById(Integer id) {
        return userRepository.findById(id).map(userMapper::map);
    }

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = userRepository.findById(id);
        if (maybeUser.isPresent()) {
            userRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserReadDto> findByFilter(UserDaoFilter userDaoFilter) {
        return userRepository.findByFilter(userDaoFilter).map(userMapper::map);
    }

    public Optional<UserReadDto> login(String email, String password) {
        String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern emailPattern = Pattern.compile(pattern);
        Matcher matcher = emailPattern.matcher(email);
        UserDaoFilter userDaoFilter;
        if (matcher.matches()) {
            userDaoFilter = new UserDaoFilter(null, email, password);
        } else {
            userDaoFilter = new UserDaoFilter(email, null, password);
        }
        return userRepository.findByFilter(userDaoFilter).map(userMapper::map);
    }

    public boolean checkIfExist(String username, String email) {
        var usernameFilter = new UserDaoFilter(username, null, null);
        var emailFilter = new UserDaoFilter(null, email, null);
        return userRepository.findByFilter(usernameFilter).isPresent() ||
               userRepository.findByFilter(emailFilter).isPresent();
    }
}

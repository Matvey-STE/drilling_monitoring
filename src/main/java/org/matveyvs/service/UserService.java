package org.matveyvs.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.mapper.UserMapper;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.entity.User;
import org.matveyvs.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;

    public Integer create(UserCreateDto userCreateDto) {
        var validate = validator.validate(userCreateDto);
        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
        var entity = userMapper.map(userCreateDto);
        log.info("Create method: " + entity);
        return userRepository.save(entity).getId();
    }

    public boolean update(UserReadDto userReadDto) {
        var validate = validator.validate(userReadDto);
        if (!validate.isEmpty()){
            throw new ConstraintViolationException(validate);
        }
        var optional = userRepository.findById(userReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            User user = userMapper.mapFull(userReadDto);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return userRepository.findById(id).map(userMapper::map);
    }

    public List<UserReadDto> findAll() {
        log.info("Find all method");
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    public boolean delete(Integer id) {
        var maybeUser = userRepository.findById(id);
        log.info("Delete method " + maybeUser);
        if (maybeUser.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserReadDto> login(String email, String password) {
        log.info("Login method for " + email);
        return userRepository.findByEmailAndPassword(email, password).map(userMapper::map);
    }

    public boolean checkIfExist(String username, String email) {
        log.info("Check if exist method for " + username + " or " + email);
        return userRepository.findByEmailOrUserName(email, username).isPresent();
    }
}

package org.matveyvs.service;

import lombok.AllArgsConstructor;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Integer create(UserCreateDto userCreateDto) {
        var entity = userMapper.map(userCreateDto);
        return userRepository.save(entity).getId();
    }

    public boolean update(UserReadDto userReadDto) {
        var optional = userRepository.findById(userReadDto.id());
        if (optional.isPresent()) {
            User user = userMapper.mapFull(userReadDto);
            userRepository.save(user);
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
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<UserReadDto> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).map(userMapper::map);
    }

    public boolean checkIfExist(String username, String email) {
        return userRepository.findByEmailOrUserName(email, username).isPresent();
    }
}

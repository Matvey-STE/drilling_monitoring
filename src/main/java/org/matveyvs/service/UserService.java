package org.matveyvs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.matveyvs.mapper.UserMapper;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.entity.User;
import org.matveyvs.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public Integer create(UserCreateDto userCreateDto) {
        var entity = userMapper.map(userCreateDto);
        log.info("Create method: " + entity);
        return userRepository.save(entity).getId();
    }

    @Transactional
    public Optional<UserReadDto> update(UserReadDto userReadDto) {
        var optional = userRepository.findById(userReadDto.id());
        log.info("Update method: " + optional);
        if (optional.isPresent()) {
            User user = userMapper.mapFull(userReadDto);
            User save = userRepository.saveAndFlush(user);
            return Optional.ofNullable(userMapper.map(save));
        }
        return Optional.empty();
    }

    public Optional<UserReadDto> findById(Integer id) {
        log.info("Find by Id method");
        return userRepository.findById(id).map(userMapper::map);
    }

    public List<UserReadDto> findAll() {
        log.info("Find all method");
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    public Page<UserReadDto> findPageWithSortAndFilter(String field,
                                                       String direction,
                                                       Integer pageNumber,
                                                       String filter) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(field).ascending() : Sort.by(field).descending();
        pageNumber = (pageNumber == null) ? 1 : pageNumber;
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                3,
                sort);
        if (filter != null) {
            return userRepository.findAllBy(filter, pageable).map(userMapper::map);
        }
        return userRepository.findAll(pageable).map(userMapper::map);
    }

    public Page<UserReadDto> findPage(int pageNumber, String filter) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 3);
        if (filter != null) {
            return userRepository.findAllBy(filter, pageable).map(userMapper::map);
        }
        return userRepository.findAll(pageable).map(userMapper::map);
    }

    public List<UserReadDto> findByKeyWord(String keyword) {
        log.info("Find all method with key word");
        return userRepository.findByKeyWord(keyword).stream().map(userMapper::map).toList();
    }

    @Transactional
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

    public Optional<UserReadDto> login(String emailOrUsername, String password) {
        if (checkIfEmail(emailOrUsername)) {
            log.info("Login method for email " + emailOrUsername);
            return userRepository
                    .findByEmailAndPassword(emailOrUsername, password).map(userMapper::map);
        }
        log.info("Login method for username " + emailOrUsername);
        return userRepository
                .findByUsernameAndPassword(emailOrUsername, password).map(userMapper::map);
    }

    private boolean checkIfEmail(String emailOrUsername) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailOrUsername);
        return matcher.matches();
    }

    public boolean checkIfExist(String username, String email) {
        log.info("Check if exist method for " + username + " or " + email);
        return userRepository.findByEmailOrUsername(email, username).isPresent();
    }
}

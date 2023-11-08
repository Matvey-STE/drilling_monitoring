package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
import org.matveyvs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.matveyvs.utils.UrlPath.LOGIN;
import static org.matveyvs.utils.UrlPath.REGISTRATION;

@Slf4j
@Controller
public class RegistrationController extends HttpServlet {
    @Autowired
    private UserService userService;

    @GetMapping(REGISTRATION)
    public ModelAndView showRegistrationPage(ModelAndView mv, HttpServletRequest request) {
        request.setAttribute("role", List.of(Role.values()));
        return mv;
    }

    @PostMapping(REGISTRATION)
    public ModelAndView register(ModelAndView mv,
                                 @RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("role") String role,
                                 @RequestParam("first_name") String firstName,
                                 @RequestParam("last_name") String lastName,
                                 HttpServletRequest request) {
        var exist = userService.checkIfExist(username, email);
        if (exist) {
            log.info("Attempt to create user: username: " + username + " or email: " + email + " are already exists");
            mv.setViewName(REGISTRATION);
        } else {
            var userDto = new UserCreateDto(username, email, password, Role.valueOf(role), Timestamp.valueOf(LocalDateTime.now()),
                    firstName, lastName);
            try {
                Integer integer = userService.create(userDto);
                log.info("User " + username + " was created with id " + integer);
                mv.setViewName(LOGIN);
            } catch (ConstraintViolationException e) {
                Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
                request.setAttribute("errors", constraintViolations);
                request.setAttribute("role", List.of(Role.values()));
                mv.setViewName(REGISTRATION);
            }
        }
        return mv;
    }
}

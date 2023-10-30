package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
import org.matveyvs.exception.ValidationException;
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

import static org.matveyvs.utils.UrlPath.LOGIN;
import static org.matveyvs.utils.UrlPath.REGISTRATION;

@Slf4j
@Controller
public class RegistrationServlet extends HttpServlet {
    @Autowired
    private UserService userService;

    @GetMapping(REGISTRATION)
    public ModelAndView showRegistrationPage(ModelAndView mv) {
        mv.addObject("role", List.of(Role.values()));
        return mv;
    }

    @PostMapping(REGISTRATION)
    public ModelAndView register(ModelAndView mv,
                                 @RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("role") String role,
                                 @RequestParam("first_name") String firstName,
                                 @RequestParam("last_name") String lastName) {
        var exist = userService.checkIfExist(username, email);
        if (exist) {
            mv.setViewName(REGISTRATION);
        } else {
            var userDto = new UserCreateDto(username, email, password, Role.valueOf(role), Timestamp.valueOf(LocalDateTime.now()),
                    firstName, lastName);
            try {
                userService.create(userDto);
                mv.setViewName(LOGIN);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        }
        return mv;
    }
}

package org.matveyvs.http.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.service.UserService;
import org.matveyvs.utils.LinkCreatorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.matveyvs.utils.UrlPath.LOGIN;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginController {
    private UserService userService;

    @GetMapping(LOGIN)
    public String showLoginPage() {
        log.info("User visited /login page");
        return "user/login";
    }

    @PostMapping(LOGIN)
    public String login(@RequestParam("username") String email,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request,
                        Model model) {
        Optional<UserReadDto> login = userService.login(email, password);
        if (login.isPresent()) {
            model.addAttribute("user", login);
            request.getSession().setAttribute("user", login);
            log.info("User with username " + login.get().username() + "successfully login");
            return "redirect:/wells";
        } else {
            String errorMessage = "Incorrect username, email, or password. Please try again.";
            redirectAttributes.addFlashAttribute("username", email);
            redirectAttributes.addFlashAttribute("loginMessage", errorMessage);
            log.info("Wrong password or username");
            return "redirect:" + LOGIN;
        }
    }
}

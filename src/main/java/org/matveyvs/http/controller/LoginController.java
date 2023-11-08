package org.matveyvs.http.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.service.UserService;
import org.matveyvs.utils.LinkCreatorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showLoginPage(@RequestParam(value = "email", required = false) String email) {
        log.info("User visited /login page");
        if (email != null) {
            log.info("Error with parameter email " + email);
        }
        return "login";
    }

    @PostMapping(LOGIN)
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpServletRequest request, Model model) {
        Optional<UserReadDto> login = userService.login(email, password);

        if (login.isPresent()) {
            model.addAttribute("user", login);
            request.getSession().setAttribute("user", login);
            log.info("User with username " + login.get().userName() + "successfully login");
            return "redirect:/wells";
        } else {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("error", "true");
            queryParams.put("email", email);

            request.setAttribute("email", email);
            request.setAttribute("error", true);

            log.info("Wrong password or username");
            return "redirect:" + LinkCreatorUtil.createLink(LOGIN, queryParams);
        }
    }
}

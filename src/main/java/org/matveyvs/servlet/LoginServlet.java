package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.matveyvs.utils.UrlPath.LOGIN;

@Slf4j
@Controller
@AllArgsConstructor
public class LoginServlet{
    private UserService userService;

    @GetMapping(LOGIN)
    public String showLoginPage() {
        return "login";
    }
    @PostMapping(LOGIN)
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpServletRequest httpServletRequest, Model model) {
        Optional<UserReadDto> login = userService.login(email, password);

        if (login.isPresent()){
            model.addAttribute("user", login);
            httpServletRequest.getSession().setAttribute("user", login);
            return "redirect:/wells";
        }
        else {
            model.addAttribute("error", true);
            model.addAttribute("email", email);
            return "redirect:/login";
        }
    }
}

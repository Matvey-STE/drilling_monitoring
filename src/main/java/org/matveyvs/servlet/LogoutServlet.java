package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

import static org.matveyvs.utils.UrlPath.LOGOUT;

@Slf4j
@Controller
public class LogoutServlet {

    @PostMapping(LOGOUT)
    public String logout(HttpServletRequest req) {
        var user = (Optional<UserReadDto>) req.getSession().getAttribute("user");
        log.info("User " + user.get().userName() + " logout");
        req.getSession().invalidate();
        return "redirect:/login";
    }
}

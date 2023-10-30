package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import static org.matveyvs.utils.UrlPath.LOGOUT;

@Slf4j
@Controller
public class LogoutServlet {

    @PostMapping(LOGOUT)
    public String login(HttpServletRequest req){
        req.getSession().invalidate();
        return "redirect:/login";
    }
}

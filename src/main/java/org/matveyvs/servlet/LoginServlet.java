package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserDto;
import org.matveyvs.service.UserService;
import org.matveyvs.utils.JspHelper;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.matveyvs.utils.UrlPath.LOGIN;

@Slf4j
@WebServlet(LOGIN)
public class LoginServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspHelper.getPath("login")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        userService.login(req.getParameter("email"), req.getParameter("password"))
                .ifPresentOrElse(userDto -> onLoginSuccess(userDto, req, resp),
                        () -> onLoginFail(req, resp));
    }

    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        log.info("Fail to login with email: " + email);
        resp.sendRedirect("/login?error=true&email=" + email);
    }

    @SneakyThrows
    private void onLoginSuccess(UserDto userDto, HttpServletRequest req, HttpServletResponse resp) {
        log.info("User " +
                 userDto +
                 " was sussesfully login " +
                 LocalDateTime.now());
        req.getSession().setAttribute("user", userDto);
        resp.sendRedirect("/wells");
    }
}

package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.CreateUserDto;
import org.matveyvs.entity.Role;
import org.matveyvs.exception.ValidationException;
import org.matveyvs.service.UserService;
import org.matveyvs.utils.JspHelper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.matveyvs.utils.UrlPath.REGISTRATION;

@Slf4j
@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", List.of(Role.values()));
        req.getRequestDispatcher(JspHelper.getPath("registration")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String existingUsername = req.getParameter("username");
        String existingEmail = req.getParameter("email");
        var exist = userService.checkIfExist(existingUsername, existingEmail);

        if (exist){
            onLoginFail(req, resp);
            log.info("The user with the username "  +
                     existingUsername +
                     " or email " +
                     existingEmail +
                     "are already exited.");
        } else {
            var userDto = new CreateUserDto(
                    req.getParameter("username"),
                    req.getParameter("email"),
                    req.getParameter("password"),
                    req.getParameter("role"),
                    Timestamp.valueOf(LocalDateTime.now()),
                    req.getParameter("first_name"),
                    req.getParameter("last_name")
            );
            try {
                userService.create(userDto);
                log.info("New user: " +
                         userDto +
                         " was successfully registered.");
                resp.sendRedirect("/login");
            } catch (ValidationException e) {
                req.setAttribute("errors", e.getErrors());
                log.error("Existing errors: " + e.getErrors());
                doGet(req, resp);
            }
        }
    }
    @SneakyThrows
    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {
        resp.sendRedirect("/registration?error=true&username=" + req.getParameter("username")
        + "&email=" + req.getParameter("email"));
    }
}

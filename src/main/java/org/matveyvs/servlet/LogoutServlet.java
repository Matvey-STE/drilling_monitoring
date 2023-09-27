package org.matveyvs.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.entity.User;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object user = req.getSession().getAttribute("user");
        log.info("User " +
                 user +
                 " logout.");
        req.getSession().invalidate();
        resp.sendRedirect("/login");
    }
}

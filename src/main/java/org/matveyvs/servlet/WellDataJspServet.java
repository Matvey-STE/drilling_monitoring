package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.WellDataService;
import org.matveyvs.utils.JspHelper;

import java.io.IOException;

@Slf4j
@WebServlet("/wells")
public class WellDataJspServet extends HttpServlet {
    private final WellDataService wellDataService = WellDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var wellData = wellDataService.findAll();
        Object user = req.getSession().getAttribute("user");
        log.info("User " +
                 user +
                 " visited /wells");
        req.setAttribute("welldata", wellData);
        req.getRequestDispatcher(JspHelper.getPath("wells")).forward(req, resp);
    }
}

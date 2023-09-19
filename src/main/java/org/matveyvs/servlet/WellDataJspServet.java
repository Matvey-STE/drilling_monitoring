package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.service.WellDataService;
import org.matveyvs.utils.JspHelper;

import java.io.IOException;

@WebServlet("/wells")
public class WellDataJspServet extends HttpServlet {
    private final WellDataService wellDataService = WellDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var wellData = wellDataService.findAll();
        req.setAttribute("welldata", wellData);
        req.getRequestDispatcher(JspHelper.getPath("wells")).forward(req, resp);
    }
}

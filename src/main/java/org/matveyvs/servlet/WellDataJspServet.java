package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.WellDataService;
import org.matveyvs.utils.JspHelper;
import org.matveyvs.utils.LinkCreatorUtil;

import java.io.IOException;

import static org.matveyvs.utils.UrlPath.WELLS;

@Slf4j
@WebServlet(WELLS)
public class WellDataJspServet extends HttpServlet {
    private final WellDataService wellDataService = WellDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var wellData = wellDataService.findAll();
        Object user = req.getSession().getAttribute("user");
        log.info("User " +
                 user +
                 " visited " +
                 LinkCreatorUtil.createLink(req.getRequestURI(), req.getQueryString()));
        req.setAttribute("welldata", wellData);
        req.getRequestDispatcher(JspHelper.getPath("wells")).forward(req, resp);
    }
}

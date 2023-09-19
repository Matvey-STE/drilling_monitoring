package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.SurfaceDataService;
import org.matveyvs.utils.JspHelper;

import java.io.IOException;

@WebServlet("/well")
public class WellJspServlet extends HttpServlet {
    private final SurfaceDataService surfaceDataService = SurfaceDataService.getInstance();
    private final DownholeDataService downholeDataService = DownholeDataService.getInstance();
    private final GammaService gammaService = GammaService.getInstance();
    private final DirectionalService directionalService = DirectionalService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String surfaceDataIdParam = req.getParameter("surfaceDataId");
        String downholeDataIdParam = req.getParameter("downholeDataId");
        String gammaIdParam = req.getParameter("gammaId");
        String directionalIdParam = req.getParameter("directionalId");
        if (surfaceDataIdParam != null) {
            var surfaceList = surfaceDataService.
                    findAllByDownholeId(Long.valueOf(surfaceDataIdParam)).stream().toList();
            req.setAttribute("surface", surfaceList);
        }
        if (downholeDataIdParam != null) {
            var downloadList = downholeDataService
                    .findAllByWellId(Long.valueOf(downholeDataIdParam)).stream().toList();
            req.setAttribute("downhole", downloadList);
        }
        if (gammaIdParam != null) {
            var gammaList = gammaService
                    .findAllByDownholeId(Long.valueOf(gammaIdParam)).stream().toList();
            req.setAttribute("gamma", gammaList);
        }
        if (directionalIdParam != null) {
            var directionalList = directionalService
                    .findAllByDownholeId(Long.valueOf(directionalIdParam)).stream().toList();
            req.setAttribute("directional", directionalList);
        }
        req.getRequestDispatcher(JspHelper.getPath("well")).forward(req, resp);
    }
}

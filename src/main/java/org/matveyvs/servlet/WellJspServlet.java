package org.matveyvs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.SurfaceDataService;
import org.matveyvs.utils.JspHelper;
import org.matveyvs.utils.LinkCreator;

import java.io.IOException;
@Slf4j
@WebServlet("/well")
public class WellJspServlet extends HttpServlet {
    private final SurfaceDataService surfaceDataService = SurfaceDataService.getInstance();
    private final DownholeDataService downholeDataService = DownholeDataService.getInstance();
    private final GammaService gammaService = GammaService.getInstance();
    private final DirectionalService directionalService = DirectionalService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String surfaceParam = "surfaceDataId";
        String downholeParam = "downholeDataId";
        String gammaParam = "gammaId";
        String directionalParam = "directionalId";

        String surfaceDataIdParam = req.getParameter(surfaceParam);
        String downholeDataIdParam = req.getParameter(downholeParam);
        String gammaIdParam = req.getParameter(gammaParam);
        String directionalIdParam = req.getParameter(directionalParam);
        Object user = req.getSession().getAttribute("user");
        if (surfaceDataIdParam != null) {
            var surfaceList = surfaceDataService.
                    findAllByDownholeId(Integer.valueOf(surfaceDataIdParam)).stream().toList();
            log.info("User " +
                     user + " visited " +
                     LinkCreator.createLink("well", surfaceParam, surfaceDataIdParam));
            req.setAttribute("surface", surfaceList);
        }
        if (downholeDataIdParam != null) {
            var downloadList = downholeDataService
                    .findAllByWellId(Integer.valueOf(downholeDataIdParam)).stream().toList();
            log.info("User " +
                     user + " visited " +
                     LinkCreator.createLink("well", downholeParam, downholeDataIdParam));
            req.setAttribute("downhole", downloadList);
        }
        if (gammaIdParam != null) {
            var gammaList = gammaService
                    .findAllByDownholeId(Integer.valueOf(gammaIdParam)).stream().toList();
            log.info("User " +
                     user + " visited " +
                     LinkCreator.createLink("well", gammaParam, gammaIdParam));
            req.setAttribute("gamma", gammaList);
        }
        if (directionalIdParam != null) {
            var directionalList = directionalService
                    .findAllByDownholeId(Integer.valueOf(directionalIdParam)).stream().toList();
            log.info("User " +
                     user + " visited " +
                     LinkCreator.createLink("well", directionalParam, directionalIdParam));
            req.setAttribute("directional", directionalList);
        }
        req.getRequestDispatcher(JspHelper.getPath("well")).forward(req, resp);
    }
}

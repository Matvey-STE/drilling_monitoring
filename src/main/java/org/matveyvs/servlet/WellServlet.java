package org.matveyvs.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.dto.DownholeDataDto;
import org.matveyvs.dto.SurfaceDataDto;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.SurfaceDataService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/well")
public class WellServlet extends HttpServlet {
    SurfaceDataService surfaceDataService = SurfaceDataService.getInstance();
    DownholeDataService downholeDataService = DownholeDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String downholeDataIdParam = req.getParameter("downholeDataId");
        String surfaceDataIdParam = req.getParameter("surfaceDataId");

        DownholeDataDto downholeDataDto = null;
        if (downholeDataIdParam != null) {
            downholeDataDto = downholeDataService.findAll().stream()
                    .filter(dao -> dao.id().equals(Long.valueOf(downholeDataIdParam)))
                    .findFirst().orElse(null);
        }

        SurfaceDataDto surfaceDataDto = null;
        if (surfaceDataIdParam != null) {
            surfaceDataDto = surfaceDataService.findAll().stream()
                    .filter(dao -> dao.id().equals(Long.valueOf(surfaceDataIdParam)))
                    .findFirst().orElse(null);
        }

        try (var writer = resp.getWriter()) {
            writer.write("<!DOCTYPE html>");
            writer.write("<html lang='en'>");
            writer.write("<head>");
            writer.write("<title>Data Information</title>");
            writer.write("<link rel='stylesheet' type='text/css' href='well.css'>");
            writer.write("</head>");
            writer.write("<body class='container'>");

            if (surfaceDataDto != null) {
                writer.write("<h1>Surface Data Information:</h1>");

                writer.write("<div>");
                writer.write("<h3>Time: " + surfaceDataDto.timestamp() + "</h3>");
                writer.write("<h3>Measure Depth: " + surfaceDataDto.measureDepth() + "</h3>");
                writer.write("<h3>Hole Depth: " + surfaceDataDto.holeDepth() + "</h3>");
                writer.write("<h3>TVD Depth: " + surfaceDataDto.tvDepth() + "</h3>");
                writer.write("</div>");
            }

            if (downholeDataDto != null) {
                writer.write("<h1>Downhole Data Information:</h1>");

                writer.write("<div>");
                writer.write("<h3>Time: " + downholeDataDto.timestamp() + "</h3>");
                writer.write("<h3>Directional Info: " + downholeDataDto.directional() + "</h3>");
                writer.write("<h3>Gamma Info: " + downholeDataDto.gamma() + "</h3>");
                writer.write("</div>");
            }

            if (surfaceDataDto == null && downholeDataDto == null) {
                writer.write("<p class='error'>No valid data specified.</p>");
            }

            writer.write("</body>");
            writer.write("</html>");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}

package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.SurfaceDataService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//@WebServlet("/well")
public class WellServlet extends HttpServlet {
    private final SurfaceDataService surfaceDataService = SurfaceDataService.getInstance();
    private final DownholeDataService downholeDataService = DownholeDataService.getInstance();
    private final GammaService gammaService = GammaService.getInstance();
    private final DirectionalService directionalService = DirectionalService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String downholeDataIdParam = req.getParameter("downholeDataId");
        String surfaceDataIdParam = req.getParameter("surfaceDataId");
        String gammaIdParam = req.getParameter("gammaId");
        String directionalIdParam = req.getParameter("directionalId");

        try (var writer = resp.getWriter()) {
            writer.write("<!DOCTYPE html>");
            writer.write("<html lang='en'>");
            writer.write("<head>");
            writer.write("<title>Data Information</title>");
            writer.write("<link rel='stylesheet' type='text/css' href='well.css'>");
            writer.write("</head>");
            writer.write("<body class='container'>");

            if (surfaceDataIdParam != null) {
                writer.write("<h1>Surface Data Information:</h1>");

                writer.write("<div>");
                writer.write("<ul>");
                surfaceDataService.findAllByDownholeId(Integer.valueOf(surfaceDataIdParam)).forEach(surfaceData ->
                        writer.write("""
                            <li>
                            <h4>Date: %s </h4>
                            <h4>Depth: %s  Hole depth: %s  TVD: %s</h4>
                            </li>""".formatted(surfaceData.measureDate(), surfaceData.measureDepth(),
                                surfaceData.holeDepth(), surfaceData.tvDepth())));
                writer.write("</ul>");
                writer.write("</div>");
            }
            if (downholeDataIdParam != null) {
                writer.write("<h1>Downhole Data Information:</h1>");
                writer.write("<ul>");
                downholeDataService.findAllByWellId(Integer.valueOf(downholeDataIdParam)).forEach(wellDataDto ->
                        writer.write("""
                            <li>
                            <h4>Company name: %s</h4>
                            <h4>Field name: %s</h4>
                            <h4>Well cluster: %s  Well: %s</h4>               
                            <h3><a href='/well?directionalId=%d'>Directional Info</a></h3>
                            <h3><a href='/well?gammaId=%d'>Gamma Info</a></h3>
                            </li>""".formatted(wellDataDto.wellData().getWell(), wellDataDto.wellData().getFieldName(),
                                wellDataDto.wellData().getWellCluster(), wellDataDto.wellData().getWell(), wellDataDto.id(),
                                wellDataDto.id())));
                writer.write("</ul>");

                writer.write("</div>");
            }
            if (gammaIdParam != null) {
                writer.write("<h1>Gamma Data Information:</h1>");
                writer.write("<div>");
                writer.write("<ul>");
                gammaService.findAllByDownholeId(Integer.valueOf(gammaIdParam)).forEach(gammaDto ->
                        writer.write("""
                                <li>
                                <h4>%s) Date: %s </h4>
                                <h4>Downhole depth: %s</h4>
                                <h4>Gamma value: %s</h4>               
                                </li>""".formatted(gammaDto.id(), gammaDto.measureDate(), gammaDto.measureDepth(), gammaDto.grcx())));
                writer.write("</ul>");
                writer.write("</div>");
            }
            if (directionalIdParam != null) {
                writer.write("<h1>Directional Data Information:</h1>");
                System.out.println(directionalService.findAllByDownholeId(Integer.valueOf(downholeDataIdParam)));
                writer.write("<div>");
                writer.write("<ul>");
                directionalService.findAllByDownholeId(Integer.valueOf(directionalIdParam)).forEach(dirDto ->
                        writer.write("""
                            <li>
                            <h4>%s) Date: %s </h4>
                            <h4>Downhole depth: %s</h4>
                            <h4>Inclination: %s Azimuth corr: %s Azimuth true: %s</h4>               
                            </li>""".formatted(dirDto.id(), dirDto.measureDate(), dirDto.measureDepth(),
                                dirDto.inc(), dirDto.azCorr(), dirDto.azTrue())));
                writer.write("</ul>");
                writer.write("</div>");
            }

            if (surfaceDataIdParam == null && downholeDataIdParam == null
                && gammaIdParam == null && directionalIdParam == null) {
                writer.write("<p class='error'>No valid data specified.</p>");
            }

            writer.write("</body>");
            writer.write("</html>");
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}

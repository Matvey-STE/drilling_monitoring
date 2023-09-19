package org.matveyvs.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.matveyvs.service.WellDataService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/wells")
public class WellDataServlet extends HttpServlet {
    private final WellDataService wellDataService = WellDataService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (var writer = resp.getWriter()) {
            writer.write("<!DOCTYPE html>");
            writer.write("<html lang='en'>");
            writer.write("<head>");
            writer.write("<title>Field Information</title>");
            writer.write("<link rel='stylesheet' type='text/css' href='styles.css'>");
            writer.write("</head>");
            writer.write("<body>");

            writer.write("<h2>Field information:</h2>");
            writer.write("<ul>");
            wellDataService.findAll().forEach(wellDataDto ->
                    writer.write("""
                            <li>
                            <h4>Company name: %s</h4>
                            <h4>Field name: %s</h4>
                            <h4>Well cluster: %s      Well: %s</h4>
                            <a href='/well?surfaceDataId=%d'>Surface Data</a>
                            <a href='/well?downholeDataId=%d'>Downhole Data</a>
                            </li>""".formatted(wellDataDto.companyName(), wellDataDto.fieldName(),
                            wellDataDto.wellCluster(), wellDataDto.well(), wellDataDto.id(), wellDataDto.id())));
            writer.write("</ul>");

            writer.write("</body>");
            writer.write("</html>");
        }
    }
}

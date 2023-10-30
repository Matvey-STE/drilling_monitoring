package org.matveyvs.servlet;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.SurfaceDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.matveyvs.utils.UrlPath.WELL;

@Slf4j
@Controller
@AllArgsConstructor
public class WellJspServlet {
    private SurfaceDataService surfaceDataService;
    private DownholeDataService downholeDataService;
    private GammaService gammaService;
    private DirectionalService directionalService;

    @GetMapping(WELL)
    public String showWellsPage(Integer surfaceDataId,
                                Integer downholeDataId,
                                Integer gammaId,
                                Integer directionalId,
                                Model model) {
        if (surfaceDataId != null) {
            var surfaceList = surfaceDataService.
                    findAllByWellId(surfaceDataId).stream().toList();
            model.addAttribute("surface", surfaceList);
        }
        if (downholeDataId != null) {
            var downloadList = downholeDataService
                    .findAllByWellId(downholeDataId).stream().toList();
            model.addAttribute("downhole", downloadList);
        }
        if (gammaId != null) {
            var gammaList = gammaService
                    .findAllByDownholeId(gammaId).stream().toList();
            model.addAttribute("gamma", gammaList);
        }
        if (directionalId != null) {
            var directionalList = directionalService
                    .findAllByDownholeId(directionalId).stream().toList();
            model.addAttribute("directional", directionalList);
        }
        return "well";
    }
}

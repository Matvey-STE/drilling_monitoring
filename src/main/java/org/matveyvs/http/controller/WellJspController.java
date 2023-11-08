package org.matveyvs.http.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.GammaService;
import org.matveyvs.service.SurfaceDataService;
import org.matveyvs.utils.LinkCreatorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static org.matveyvs.utils.UrlPath.WELL;

@Slf4j
@Controller
@AllArgsConstructor
public class WellJspController {
    private SurfaceDataService surfaceDataService;
    private DownholeDataService downholeDataService;
    private GammaService gammaService;
    private DirectionalService directionalService;

    @GetMapping(WELL)
    public String showWellsPage(Integer surfaceDataId,
                                Integer downholeDataId,
                                Integer gammaId,
                                Integer directionalId,
                                Model model, HttpServletRequest request) {
        var user = (Optional<UserReadDto>) request.getSession().getAttribute("user");
        String username = null;
        if (user.isPresent()) {
            username = user.get().userName();
        }

        if (surfaceDataId != null) {
            var surfaceList = surfaceDataService.
                    findAllByWellId(surfaceDataId).stream().toList();
            model.addAttribute("surface", surfaceList);
            log.info("User " + username + " visited " +
                     LinkCreatorUtil.createLink(WELL, "surfaceDataId", surfaceDataId.toString()));
        }
        if (downholeDataId != null) {
            var downloadList = downholeDataService
                    .findAllByWellId(downholeDataId).stream().toList();
            model.addAttribute("downhole", downloadList);
            log.info("User " + username + " visited " +
                     LinkCreatorUtil.createLink(WELL, "downholeDataId", downholeDataId.toString()));
        }
        if (gammaId != null) {
            var gammaList = gammaService
                    .findAllByDownholeId(gammaId).stream().toList();
            model.addAttribute("gamma", gammaList);
            log.info("User " + username + " visited " +
                     LinkCreatorUtil.createLink(WELL, "gammaId", gammaId.toString()));
        }
        if (directionalId != null) {
            var directionalList = directionalService
                    .findAllByDownholeId(directionalId).stream().toList();
            model.addAttribute("directional", directionalList);
            log.info("User " + username + " visited " +
                     LinkCreatorUtil.createLink(WELL, "directionalId", directionalId.toString()));
        }
        return "well";
    }
}

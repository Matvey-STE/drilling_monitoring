package org.matveyvs.http.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.SurfaceDataService;
import org.matveyvs.service.WellDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@AllArgsConstructor
public class SurfaceDataController {
    private SurfaceDataService surfaceDataService;
    private WellDataService wellDataService;

    @GetMapping("/wells/{id}/surface")
    public String showSurfaceDataList(Model model,
                                  @PathVariable("id") Integer id) {
        model.addAttribute("surfaceData", surfaceDataService.findAllByWellId(id));
        model.addAttribute("well", wellDataService.findById(id));
        return "/monitoring/surfaceDataList";
    }
}

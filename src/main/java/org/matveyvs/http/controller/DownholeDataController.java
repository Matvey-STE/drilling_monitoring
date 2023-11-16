package org.matveyvs.http.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.DownholeDataService;
import org.matveyvs.service.WellDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@AllArgsConstructor
public class DownholeDataController {
    private WellDataService wellDataService;
    private DownholeDataService downholeDataService;

    @GetMapping("/wells/{id}/downhole")
    public String showSurfaceData(Model model,
                                  @PathVariable("id") Integer id) {
        model.addAttribute("downhole", downholeDataService.findAllByWellId(id));
        model.addAttribute("well", wellDataService.findById(id));
        return "monitoring/downholeData";
    }
}

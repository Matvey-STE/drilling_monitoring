package org.matveyvs.http.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.service.DirectionalService;
import org.matveyvs.service.WellDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@AllArgsConstructor
public class DirectionalController {
    private DirectionalService directionalService;
    private WellDataService wellDataService;

    @GetMapping("/wells/{wellId}/downhole/{downholeId}/directional")
    public String showDirectionalList(Model model,
                                  @PathVariable("wellId") Integer wellId,
                                  @PathVariable("downholeId") Integer dowholeId) {
        model.addAttribute("directional", directionalService.findAllByDownholeId(dowholeId));
        model.addAttribute("well", wellDataService.findById(wellId));
        return "/monitoring/directionalList";
    }
}

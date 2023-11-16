package org.matveyvs.http.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.WellDataCreateDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.service.WellDataService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.matveyvs.utils.UrlPath.*;

@Slf4j
@Controller
@AllArgsConstructor
public class WellDataController {
    private WellDataService wellDataService;

    @GetMapping(WELLS)
    public String showWellsPage(Model model) {
        List<WellDataReadDto> wellData = wellDataService.findAll();
        model.addAttribute("welldata", wellData);
        return "/monitoring/wells";
    }

    @GetMapping("/wellAdd")
    public String createWell(Model model, @ModelAttribute("well") WellDataCreateDto wellData) {
        model.addAttribute("well", wellData);
        return "/monitoring/wellAdd";
    }

    @PostMapping("/wellAdd")
    public String create(@ModelAttribute @Validated WellDataCreateDto wellDataDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("well", wellDataDto);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/wellAdd";
        }
        wellDataService.create(wellDataDto);
        return "redirect:/wells";
    }

    @GetMapping("/wellEdit/{id}")
    public String editById(@PathVariable Integer id, Model model) {
        return wellDataService.findById(id).map(wellData -> {
                    model.addAttribute("well", wellData);
                    return "/monitoring/wellEdit";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/wells/{id}/update")
    public String update(@ModelAttribute @Validated WellDataReadDto dataReadDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/wellEdit/{id}";
        }
        return wellDataService.update(dataReadDto)
                .map(it -> "redirect:/wellEdit/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/wellDetails/{id}")
    public String detailsById(@PathVariable Integer id, Model model) {
        return wellDataService.findById(id).map(wellData -> {
                    model.addAttribute("well", wellData);
                    return "/monitoring/wellDetails";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/wells/{id}/delete", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String delete(@PathVariable("id") Integer id) {
        if (!wellDataService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/wells";
    }

}

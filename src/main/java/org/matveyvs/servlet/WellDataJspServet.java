package org.matveyvs.servlet;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.dto.WellDataReadDto;
import org.matveyvs.service.WellDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

import static org.matveyvs.utils.UrlPath.*;

@Slf4j
@Controller
@AllArgsConstructor
public class WellDataJspServet {
    private WellDataService wellDataService;

    @GetMapping(WELLS)
    public String showWellsPage(Model model, HttpServletRequest request) {
        var user = (Optional<UserReadDto>) request.getSession().getAttribute("user");
        log.info("User " + user.get().userName() + " visited " + WELLS + "page");
        List<WellDataReadDto> wellData = wellDataService.findAll();
        model.addAttribute("welldata", wellData);
        return "wells";
    }
}

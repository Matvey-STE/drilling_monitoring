package org.matveyvs.http.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.entity.Role;
import org.matveyvs.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.matveyvs.utils.UrlPath.*;

@Slf4j
@Controller
@AllArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping(REGISTRATION)
    public String showRegistrationPage(Model model,
                                       @ModelAttribute("user") UserCreateDto userDto) {
        model.addAttribute("user", userDto);
        model.addAttribute("roles", List.of(Role.values()));
        model.addAttribute("dateOfCreation", Timestamp.valueOf(LocalDateTime.now()));
        return "user/registration";
    }

    @PostMapping(REGISTRATION)
    public String register(@ModelAttribute @Validated UserCreateDto userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        var userReadDto = userService.create(userDto);
        return "redirect:/users";
    }
}

package org.matveyvs.http.controller;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matveyvs.dto.UserCreateDto;
import org.matveyvs.dto.UserReadDto;
import org.matveyvs.entity.Role;
import org.matveyvs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public String getAllPages(Model model,
                              @PathParam("keyword") String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        return getAllWithSort(model, 1, "id", "asc", keyword);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String getAllWithSort(Model model,
                                 @PathVariable("pageNumber") int currentPage,
                                 @PathParam("sortField") String sortField,
                                 @PathParam("sortDir") String sortDir,
                                 @PathParam("keyword") String keyword) {
        var page = userService
                .findPageWithSortAndFilter(sortField, sortDir, currentPage, keyword);
        var userReadDtos = page.getContent();
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("keyword", keyword);

        model.addAttribute("users", userReadDtos);
        return "/admin/userList";
    }

    @GetMapping("/userAdd")
    public String createUser(Model model, @ModelAttribute("user") UserCreateDto userCreateDto) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", userCreateDto);
        return "/admin/userAdd";
    }

    @PostMapping("/userAdd")
    public String create(@ModelAttribute @Validated UserCreateDto userCreateDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("user", userCreateDto);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/userAdd";
        }
        try {
            var id = userService.create(userCreateDto);
            log.info("User" + userCreateDto + " with id: " + id + " was created");
        } catch (Exception exception) {
            if (exception.getMessage().contains("unique constraint")) {
                String message = "Username or email are already exists";
                ObjectError error = new ObjectError("exists.constraint", message);
                bindingResult.addError(error);
            }
            redirectAttributes.addFlashAttribute("user", userCreateDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/userAdd";
        }
        return "redirect:/users";
    }

    @PostMapping(value = "/users/{id}/update")
    public String update(@ModelAttribute @Validated UserReadDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/userEdit/{id}";
        }
        return userService.update(user)
                .map(it -> "redirect:/userEdit/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/users/{id}/delete", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String delete(@PathVariable("id") Integer id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users";
    }

    @GetMapping("/userEdit/{id}")
    public String editById(Model model,
                           @PathVariable("id") int id) {
        return userService.findById(id).map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "/admin/userEdit";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/userDetails/{id}")
    public String detailsById(Model model, @PathVariable int id) {
        return userService.findById(id).map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "/admin/userDetails";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}

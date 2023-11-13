package org.matveyvs.http.controller.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

    @GetMapping("/index")
    public String index() {
        return "/_layout";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin/index";
    }

    @GetMapping("/techsupport")
    public String techsupport() {
        return "/techsupport/index";
    }

    @GetMapping("/monitoring")
    public String monitoring() {
        return "/monitoring/index";
    }

    @GetMapping("/settings")
    public String settings() {
        return "/settings/index";
    }

}

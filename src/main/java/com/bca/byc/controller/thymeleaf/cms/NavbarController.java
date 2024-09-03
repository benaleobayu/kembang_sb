package com.bca.byc.controller.thymeleaf.cms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavbarController {

    @GetMapping("/sidebar")
    public String navbar(Model model) {
        model.addAttribute("routePrefix", "yourRoutePrefix"); // Replace with actual route prefix
        model.addAttribute("navbarStyle", "dark"); // Example style
        return "layout";
    }
}

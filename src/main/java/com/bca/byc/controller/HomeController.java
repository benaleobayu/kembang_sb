package com.bca.byc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("sample")
    public String home(Model model) {
        model.addAttribute("title", "Home Page");
        return "index";
    }
}

package com.bca.byc.controller.thymeleaf.cms;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleController {

    @GetMapping("/simple")
    public String showSimplePage(Model model) {
        // You can add model attributes if needed
        return "content"; // This refers to content.html
    }
}

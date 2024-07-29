package com.bca.byc.controller.cms;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms/dashboard")
public class DashboardController {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String viewDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        model.addAttribute("title", "Dashboard");
        return "cms/dashboard";   
    }
}

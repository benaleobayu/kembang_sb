package com.bca.byc.controller.thymeleaf.cms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/cms/data-analytic")
public class DashboardController {
//    @PreAuthorize("hasPermission(#authentication, 'VIEW_DASHBOARD')") // Use your permission name
    @GetMapping
    public String viewDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        model.addAttribute("username", authentication.getName());
//        model.addAttribute("authorities", authentication.getAuthorities());

        Map<String, List<String>> groupedAuthorities = new LinkedHashMap<>();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String[] parts = authority.getAuthority().split("\\.");
            if (parts.length == 2) {
                String resource = parts[0];
                String action = parts[1];
                groupedAuthorities.computeIfAbsent(resource, k -> new ArrayList<>()).add(action);
            }
        }

        List<AuthorityGroup> authorityGroups = new ArrayList<>();
        int count = 1;
        for (Map.Entry<String, List<String>> entry : groupedAuthorities.entrySet()) {
            authorityGroups.add(new AuthorityGroup(count++, entry.getKey(), String.join(", ", entry.getValue())));
        }
        model.addAttribute("authorityGroups", authorityGroups);
        return "cms/dashboard";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorityGroup {
        private int id;
        private String resource;
        private String actions;
    }
}

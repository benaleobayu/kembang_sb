package com.bca.byc.controller.cms;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;


@Controller
@RequestMapping("/cms/dashboard")
public class DashboardController {
    // @PreAuthorize("hasPermission(#authentication, 'VIEW_DASHBOARD')") // Use your permission name
    @GetMapping
    public String viewDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());

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
        model.addAttribute("title", "Dashboard");
        return "cms/dashboard";   
    }

    public static class AuthorityGroup {
        private int id;
        private String resource;
        private String actions;

        public AuthorityGroup(int id, String resource, String actions) {
            this.id = id;
            this.resource = resource;
            this.actions = actions;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getActions() {
            return actions;
        }

        public void setActions(String actions) {
            this.actions = actions;
        }
    }
}

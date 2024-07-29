package com.bca.byc.config;

import com.bca.byc.security.AdminPrincipal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    
    @Value("${app.base.url}")
    private String baseUrl;


    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication authentication = getCurrentAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminPrincipal) {
            AdminPrincipal adminPrincipal = (AdminPrincipal) authentication.getPrincipal();
            var routePrefix = "cms";
            model.addAttribute("admin", adminPrincipal.getAdmin());
            // model.addAttribute("dashboardUrl", routePrefix + "/dashboard");
            model.addAttribute("dashboardUrl", baseUrl + routePrefix + "/dashboard");
            model.addAttribute("auditTrailurl", baseUrl + routePrefix + "/report/audit-trail");
            model.addAttribute("adminsUrl", baseUrl + routePrefix + "/admins");
            model.addAttribute("rolesUrl", baseUrl + routePrefix + "/roles");

            
        };
    }

    private Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

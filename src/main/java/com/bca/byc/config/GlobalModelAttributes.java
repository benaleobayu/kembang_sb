package com.bca.byc.config;

import com.bca.byc.security.AdminPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication authentication = getCurrentAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AdminPrincipal) {
            AdminPrincipal adminPrincipal = (AdminPrincipal) authentication.getPrincipal();
            model.addAttribute("admin", adminPrincipal.getAdmin());
        }
    }

    private Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

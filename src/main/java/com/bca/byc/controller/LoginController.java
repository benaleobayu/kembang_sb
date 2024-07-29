package com.bca.byc.controller;

import com.bca.byc.dto.LoginRequest;
// import com.bca.byc.entity.Admin;
import com.bca.byc.repository.AdminRepository;
// import com.bca.byc.security.AdminPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
// import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    // @Autowired
    // private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/")
    public String index(Model model) {
        // Redirect to the login page
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // Redirect authenticated users to the dashboard
            return "redirect:/cms/dashboard";
        }
        logger.info("GET /login endpoint hit");
        model.addAttribute("loginForm", new LoginRequest());
        return "login";
    }
    
}

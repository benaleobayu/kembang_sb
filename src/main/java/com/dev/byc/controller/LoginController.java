package com.dev.byc.controller;

import com.dev.byc.dto.LoginRequest;
import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import com.dev.byc.security.AdminPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    @Autowired
    private AdminRepository adminRepository;

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
    @PostMapping("/login")
    public String loginSubmit(LoginRequest loginRequest, Model model) {
        logger.info("POST /login endpoint hit");
        logger.info("LoginRequest email: " + loginRequest.getEmail());
        Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());
        logger.info("AdminRepository findByEmail called");

        if (adminOptional.isPresent()) {
            logger.info("Admin found with email: " + loginRequest.getEmail());
            Admin admin = adminOptional.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                logger.info("Password matches for admin: " + loginRequest.getEmail());

                AdminPrincipal adminPrincipal = new AdminPrincipal(admin);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(adminPrincipal, null, adminPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                logger.info("Authentication successful for admin: " + loginRequest.getEmail());
                adminPrincipal.getAuthorities().forEach(authority -> 
                    logger.info("Authority: " + authority.getAuthority())
                );
                return "redirect:/cms/dashboard"; // Pastikan URL ini bukan bagian dari loop pengalihan
            } else {
                logger.warn("Password does not match for admin: " + loginRequest.getEmail());
                model.addAttribute("error", "Invalid email or password");
                loginRequest.setPassword("");
                model.addAttribute("loginForm", loginRequest);
                return "login";
            }
        } else {
            
            logger.warn("Admin not found with email: " + loginRequest.getEmail());
            loginRequest.setPassword("");
            model.addAttribute("loginForm", loginRequest);
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}

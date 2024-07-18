package com.dev.byc.controller;

import com.dev.byc.model.LoginForm;
import com.dev.byc.model.SuccessResponse;
import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public SuccessResponse loginSubmit(@ModelAttribute LoginForm loginForm, Model model) {
        Admin admin = adminRepository.findByEmail(loginForm.getEmail());

        SuccessResponse response = new SuccessResponse(false, null);
        if (admin != null && passwordEncoder.matches(loginForm.getPassword(), admin.getPassword())) {
            // Login successful, return success response
            response.setResult(true);
            response.setMessage("Login successful");
           //return "{\"status\":\"success\"}";
        } else {
            response.setResult(false);
            response.setMessage("Invalid username or password");
            // Login failed, return error response
            //return "{\"status\":\"error\", \"message\":\"Invalid username or password\"}";
        }
        return response;
    }
}

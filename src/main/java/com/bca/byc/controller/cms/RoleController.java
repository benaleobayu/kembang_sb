package com.bca.byc.controller.cms;

import com.bca.byc.entity.Role;
import com.bca.byc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cms/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String showAllRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("resourceName", "roles");
        model.addAttribute("title","Roles");
        return "cms/roles/index"; // Ensure you have a Thymeleaf template named index.html under roles directory
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/create"; // Ensure you have a Thymeleaf template named create.html under roles directory
    }

    @PostMapping
    public String createRole(@ModelAttribute Role role) {
        roleService.createRole(role);
        return "redirect:/cms/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Role role = roleService.getRoleById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        model.addAttribute("role", role);
        return "roles/edit"; // Ensure you have a Thymeleaf template named edit.html under roles directory
    }

    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute Role roleDetails) {
        roleService.updateRole(id, roleDetails);
        return "redirect:/cms/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/cms/roles";
    }
}

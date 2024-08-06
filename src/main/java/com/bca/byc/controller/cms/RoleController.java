package com.bca.byc.controller.cms;

import com.bca.byc.entity.Role;
import com.bca.byc.model.LocationCreate;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.LocationService;
import com.bca.byc.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms/roles")
public class RoleController {

    private static final String resourceName = "roles";

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasPermission(#authentication, 'roles.view')") // Use your permission name
    @GetMapping
    public String showAllRoles(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("resourceName", resourceName);
        model.addAttribute("title", "Roles");
        return "cms/roles/index"; // Ensure you have a Thymeleaf template named index.html under roles directory
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.view')")
    @GetMapping("/all")
    @ResponseBody
    public Map<String, Object> getAllRoles(@RequestParam int draw, @RequestParam int start, @RequestParam int length) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);
        Page<Role> rolePage = roleService.getRolePagination(pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", rolePage.getTotalElements());
        response.put("recordsFiltered", rolePage.getTotalElements());
        response.put("data", rolePage.getContent());
        return response;
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.create')") // Use your permission name
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "cms/roles/create"; // Ensure you have a Thymeleaf template named create.html under roles directory
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.create')") // Use your permission name
    @PostMapping
    public String createRole(@ModelAttribute Role role) {
        roleService.createRole(role);
        return "redirect:/cms/roles";
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.update')") // Use your permission name
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Role role = roleService.getRoleById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        model.addAttribute("role", role);
        return "roles/edit"; // Ensure you have a Thymeleaf template named edit.html under roles directory
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.update')") // Use your permission name
    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute Role roleDetails) {
        roleService.updateRole(id, roleDetails);
        return "redirect:/cms/roles";
    }

    @PreAuthorize("hasPermission(#authentication, 'roles.delete')") // Use your permission name
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return "redirect:/cms/roles";
    }

}

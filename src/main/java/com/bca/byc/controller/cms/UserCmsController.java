package com.bca.byc.controller.cms;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.User;
import com.bca.byc.model.user.UserDetailResponse;
import com.bca.byc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cms/users")
public class UserCmsController {

    private UserDTOConverter userDTOConverter;

    private static final String resourceName = "users";

    @Autowired
    private UserService userService;

    @PreAuthorize("hasPermission(#authentication, 'users.view')") // Use your permission name
    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userService.findAllUsers(100);


        model.addAttribute("users", users);
        model.addAttribute("resourceName", resourceName);
        model.addAttribute("title", "Users");
        return "cms/users/index"; // Ensure you have a Thymeleaf template named index.html under users directory
    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.view')")
//    @GetMapping("/all")
//    @ResponseBody
//    public Map<String, Object> getAllUsers(@RequestParam int draw, @RequestParam int start, @RequestParam int length) {
//        int page = start / length;
//        Pageable pageable = PageRequest.of(page, length);
//        Page<User> userPage = userService.getUserPagination(pageable);
//        Map<String, Object> response = new HashMap<>();
//        response.put("draw", draw);
//        response.put("recordsTotal", userPage.getTotalElements());
//        response.put("recordsFiltered", userPage.getTotalElements());
//        response.put("data", userPage.getContent());
//        return response;
//    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.create')") // Use your permission name
//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
//        model.addAttribute("user", new User());
//        return "cms/users/create"; // Ensure you have a Thymeleaf template named create.html under users directory
//    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.create')") // Use your permission name
//    @PostMapping
//    public String createUser(@ModelAttribute User user) {
//        userService.createUser(user);
//        return "redirect:/cms/users";
//    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.update')") // Use your permission name
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        model.addAttribute("user", user);
//        return "users/edit"; // Ensure you have a Thymeleaf template named edit.html under users directory
//    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.update')") // Use your permission name
//    @PostMapping("/update/{id}")
//    public String updateUser(@PathVariable Long id, @ModelAttribute User userDetails) {
//        userService.updateUser(id, userDetails);
//        return "redirect:/cms/users";
//    }
//
//    @PreAuthorize("hasPermission(#authentication, 'users.delete')") // Use your permission name
//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "redirect:/cms/users";
//    }

}

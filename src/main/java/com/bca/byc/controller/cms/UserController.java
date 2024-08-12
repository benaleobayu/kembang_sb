package com.bca.byc.controller.cms;

import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.UserService;
import com.bca.byc.service.component.SidebarMenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cms/users")
public class UserController {

    private UserRepository repository;

    @GetMapping
    @PreAuthorize("hasPermission(#authentication, 'users.view')")
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("User", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", repository.findAll());

        // some part
        model.addAttribute("tableName", "Users");

        // get location view
        return "cms/user/all";
    }

    @GetMapping("/create")
    @PreAuthorize("hasPermission(#authentication, 'users.view')")
    public String create(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("User", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        // get location view
        return "cms/user/create";
    }



}

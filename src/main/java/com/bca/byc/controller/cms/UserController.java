package com.bca.byc.controller.cms;

import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.component.SidebarMenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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

    private SidebarMenuService menuService;

    private UserRepository repository;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // required for show the sidebar
        model.addAttribute("menuGroups", menuService.getSidebarMenuList());

        // set active page
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);

        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("User", currentUrl, true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", repository.findAll());

        // some part
        model.addAttribute("tableName", "Users");

        // get location view
        return "cms/user/index";
    }

}

package com.bca.byc.config;

import com.bca.byc.service.component.SidebarMenuService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class GlobalSidebarController {

    private SidebarMenuService menuService;

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpServletRequest request) {
//        List<SidebarMenuService.MenuGroup> menuGroups = menuService.getSidebarMenuList();
//        model.addAttribute("menuGroups", menuGroups);
//
//        // set active page
//        String currentUrl = request.getRequestURI();
//        model.addAttribute("currentUrl", currentUrl);


    }

}


package com.bca.byc.controller.cms;

import com.bca.byc.service.component.SidebarMenuService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@AllArgsConstructor
public class GlobalSidebarController {

    private SidebarMenuService menuService;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        List<SidebarMenuService.MenuGroup> menuGroups = menuService.getSidebarMenuList();
        model.addAttribute("menuGroups", menuGroups);
    }

}


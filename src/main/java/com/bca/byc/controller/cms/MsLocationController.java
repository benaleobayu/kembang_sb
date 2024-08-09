package com.bca.byc.controller.cms;

import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.component.SidebarMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/cms/ms/location")
public class MsLocationController {

    private SidebarMenuService menuService;

    private LocationRepository repository;

    @GetMapping
    public String index(Model model) {
        // required for show the sidebar
        model.addAttribute("menuGroups", menuService.getSidebarMenuList());
        //table
        model.addAttribute("datas", repository.findAll());

        // some part
        model.addAttribute("tableName", "locations");

        // get location view
        return "cms/ms/location/index";
    }

}

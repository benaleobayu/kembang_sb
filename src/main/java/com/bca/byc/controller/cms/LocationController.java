package com.bca.byc.controller.cms;

import com.bca.byc.entity.Location;
import com.bca.byc.model.cms.LocationDetailResponse;
import com.bca.byc.service.ms.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cms/admin/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    private static final String resourceName = "locations";

//    @PreAuthorize("hasPermission(#authentication, 'locations.view')") // Use your permission name
    @GetMapping
    public String showAllData(Model model) {

        List<Location> locations = locationService.findAllLocations();

        model.addAttribute("locations", locations);
        model.addAttribute("resourceName", resourceName);
        model.addAttribute("title", "Locations");

        return "cms/ms/location/index";
    }
}

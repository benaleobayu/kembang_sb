package com.bca.byc.controller.cms;

import com.bca.byc.model.cms.LocationModelDTO;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cms/ms/location")
public class MsLocationController {

    @Autowired
    private LocationService service;

    private final String thisUrl = "/cms/ms/location";

    // get route index table
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master Location", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        List<LocationModelDTO.DetailResponse> alldata = service.findAllData();
        for (LocationModelDTO.DetailResponse item : alldata) {
            if (item.getDescription() != null) {
                String cleanDescription = Jsoup.parse(item.getDescription()).text();
                item.setDescription(cleanDescription);
            }
        }

        //table
        model.addAttribute("datas", alldata);

        // some part
        model.addAttribute("tableName", "Master Location");
        model.addAttribute("modelName", "location");
        return thisUrl + "/index";
    }

    // get route detail data
    @GetMapping("/{id}/detail")
    public String view(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master Location", thisUrl, false),
                new Breadcrumb("Details", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        LocationModelDTO.DetailResponse data = service.findDataById(id);
        model.addAttribute("formData", data);
        model.addAttribute("formMode", "view");
        return thisUrl + "/form_location";
    }

    // get route create data
    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master Location", thisUrl, false),
                new Breadcrumb("Create", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        LocationModelDTO.CreateRequest dto = new LocationModelDTO.CreateRequest();
        model.addAttribute("formData", dto);
        model.addAttribute("modelName", "location");
        model.addAttribute("formMode", "create");
        return thisUrl + "/form_location";
    }

    // get method create data
    @PostMapping("/create")
    public String create(@ModelAttribute("formData") @Valid LocationModelDTO.CreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/create";
        }

        service.saveData(dto);
        return "redirect:/cms/ms/location";
    }

    // get route edit data
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master Location", thisUrl, false),
                new Breadcrumb("Edit", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        LocationModelDTO.DetailResponse dto = service.findDataById(id);
        model.addAttribute("formData", dto);
        model.addAttribute("formMode", "update");
        model.addAttribute("modelName", "location");
        return thisUrl + "/form_location";
    }

    // get method edit data
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, @ModelAttribute("formData") @Valid LocationModelDTO.UpdateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/" + id + "/edit";
        }
        service.updateData(id, dto);
        return "redirect:/cms/ms/location";
    }


}

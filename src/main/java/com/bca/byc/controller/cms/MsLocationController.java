package com.bca.byc.controller.cms;

import com.bca.byc.model.cms.LocationModelDTO;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.service.MsLocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(MsLocationController.thisUrl)
public class MsLocationController {

    private static final String suffixName = "location";
    public static final String thisUrl = "/cms/ms/" + MsLocationController.suffixName;
    private final String titlePage = "Location";
    @Autowired
    private MsLocationService service;

    // get route index table
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        List<LocationModelDTO.DetailResponse> alldata = service.findAllData();
        model.addAttribute("datas", alldata);

        // some part
        model.addAttribute("titlePage", titlePage);
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/index";
    }

    // get route detail data
    @GetMapping("/{id}/detail")
    public String view(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Details", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        LocationModelDTO.DetailResponse data = service.findDataById(id);
        model.addAttribute("formData", data);
        model.addAttribute("formMode", "view");
        return thisUrl + "/form_data";
    }

    // get route create data
    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Create", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        LocationModelDTO.CreateRequest dto = new LocationModelDTO.CreateRequest();
        model.addAttribute("formData", dto);
        model.addAttribute("modelName", suffixName);
        model.addAttribute("formMode", "create");
        return thisUrl + "/form_data";
    }

    // get method create data
    @PostMapping("/create")
    public String create(@ModelAttribute("formData") @Valid LocationModelDTO.CreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/create";
        }

        service.saveData(dto);
        return "redirect:/cms/ms/" + suffixName;
    }

    // get route edit data
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Edit", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        LocationModelDTO.DetailResponse dto = service.findDataById(id);
        model.addAttribute("formData", dto);
        model.addAttribute("formMode", "update");
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/form_data";
    }

    // get method edit data
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, @ModelAttribute("formData") @Valid LocationModelDTO.UpdateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/" + id + "/edit";
        }
        service.updateData(id, dto);
        return "redirect:/cms/ms/" + suffixName;
    }


}

package com.bca.byc.controller.cms;

import com.bca.byc.model.InterestCategoryModelDTO;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.service.MsInterestCategoryService;
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
@RequestMapping(MsInterestCategoryController.thisUrl)
public class MsInterestCategoryController {

    private static final String prefixName = "cms/ms/";
    private static final String suffixName = "interest_category";
    static final String thisUrl = prefixName + suffixName;
    private final String titlePage = "Interest Category";

    @Autowired
    private MsInterestCategoryService service;

    // get route index table
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        List<InterestCategoryModelDTO.InterestCategoryDetailResponse> alldata = service.findAllData();
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

        InterestCategoryModelDTO.InterestCategoryDetailResponse data = service.findDataById(id);
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
        InterestCategoryModelDTO.InterestCategoryCreateRequest dto = new InterestCategoryModelDTO.InterestCategoryCreateRequest();
        model.addAttribute("formData", dto);
        model.addAttribute("modelName", suffixName);
        model.addAttribute("formMode", "create");
        return thisUrl + "/form_data";
    }

    // get method create data
    @PostMapping("/create")
    public String create(@ModelAttribute("formData") @Valid InterestCategoryModelDTO.InterestCategoryCreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
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
        InterestCategoryModelDTO.InterestCategoryDetailResponse dto = service.findDataById(id);
        model.addAttribute("formData", dto);
        model.addAttribute("formMode", "update");
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/form_data";
    }

    // get method edit data
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, @ModelAttribute("formData") @Valid InterestCategoryModelDTO.InterestCategoryUpdateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/" + id + "/edit";
        }
        service.updateData(id, dto);
        return "redirect:/cms/ms/" + suffixName;
    }


}

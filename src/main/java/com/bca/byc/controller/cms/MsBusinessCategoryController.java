package com.bca.byc.controller.cms;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessCategoryModelDTO;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.service.MsBusinessCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(MsBusinessCategoryController.thisUrl)
public class MsBusinessCategoryController {

    private static final String prefixName = "cms/ms/";
    private static final String suffixName = "business_category";
    static final String thisUrl =  prefixName + suffixName;
    private final String titlePage = "Business Category";

    private final MsBusinessCategoryService service;
    private final BusinessCategoryRepository repository;

    // get route index table
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> alldata = service.findByParentIdIsNull();
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

        BusinessCategoryModelDTO.BusinessCategoryDetailResponse data = service.findDataById(id);
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
        BusinessCategoryModelDTO.BusinessCategoryCreateRequest data = new BusinessCategoryModelDTO.BusinessCategoryCreateRequest();
        model.addAttribute("formData", data);
        model.addAttribute("modelName", suffixName);
        model.addAttribute("formMode", "create");
        return thisUrl + "/form_data";
    }

    // get method create data
    @PostMapping("/create")
    public String create(@ModelAttribute("formData") @Valid BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/create";
        }

        service.saveData(dto);
        return "redirect:/cms/ms/" + suffixName;
    }

    // get route edit data
    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Create Children", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        BusinessCategoryModelDTO.BusinessCategoryDetailResponse data = service.findDataById(id);
        model.addAttribute("formData", data);
        model.addAttribute("formMode", "edit");
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/form_data";
    }


    // get method edit data
    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id, @ModelAttribute("formData") @Valid BusinessCategoryModelDTO.BusinessCategoryUpdateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/" + id + "/edit";
        }
        service.updateData(id, dto);
        return "redirect:/cms/ms/" + suffixName;
    }

    // ---------------------------------------- child ----------------------------------------

    // get route index table
    @GetMapping("/{parentId}/child")
    public String index_child(@PathVariable("parentId") Long parentId, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, request.getRequestURI(), false),
                new Breadcrumb("Child " + titlePage, request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // if not valid parentId
        BusinessCategory parentCategory = repository.findById(parentId)
                .orElseThrow(() -> new BadRequestException("Invalid parentId"));
        //table
        List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> alldata = service.findByParent(parentCategory);
        model.addAttribute("datas", alldata);

        // some part
        model.addAttribute("titlePage", "child of" + titlePage);
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/child/index";
    }

    // get route detail child data
    @GetMapping("/{id}/child/{childId}detail")
    public String view_child(@PathVariable("id") Long id, @PathVariable("childId") Long childId, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Details", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);
        // continue to the service
        BusinessCategoryModelDTO.BusinessCategoryDetailResponse data = service.findDataById(childId);
        // check if child belongs the parent
        if (data.getParentId() == null || !data.getParentId().equals(id)){
            throw new IllegalArgumentException("Invalid data");
        }
        model.addAttribute("formData", data);
        model.addAttribute("formMode", "view");
        return thisUrl + "/child/form_data";
    }

    // get route create data
    @GetMapping("/{id}/child/create")
    public String create_child(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Create", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto = new BusinessCategoryModelDTO.BusinessCategoryCreateRequest();
        model.addAttribute("formData", dto);
        model.addAttribute("modelName", suffixName);
        model.addAttribute("formMode", "create");
        return thisUrl + "/form_data";
    }

    // get method create data
    @PostMapping("/{id}/child/create")
    public String create_child(@ModelAttribute("formData") @Valid BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/create";
        }

        service.saveData(dto);
        return "redirect:/cms/ms/" + suffixName;
    }

    // get route create_children data
    @GetMapping("/{id}/child/{childId}/edit")
    public String update_child(@PathVariable("childId") Long childId, Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false),
                new Breadcrumb("Master " + titlePage, thisUrl, false),
                new Breadcrumb("Create Children", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // some part
        BusinessCategoryModelDTO.BusinessCategoryCreateRequest dto = new BusinessCategoryModelDTO.BusinessCategoryCreateRequest();
        model.addAttribute("formData", dto);
        model.addAttribute("formMode", "create");
        model.addAttribute("modelName", suffixName);
        return thisUrl + "/form_data";
    }


    // get method edit data
    @PostMapping("/{id}/child/{childId}/edit")
    public String update_child(@PathVariable("id") Long id, @ModelAttribute("formData") @Valid BusinessCategoryModelDTO.BusinessCategoryUpdateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("formData", dto);
            return thisUrl + "/" + id + "/edit";
        }
        service.updateData(id, dto);
        return "redirect:/cms/ms/" + suffixName;
    }


}

package com.bca.byc.controller.cms;

import com.bca.byc.model.cms.LocationModelDTO;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.model.component.FieldList;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cms/ms/location")
public class MsLocationController {

    @Autowired
    private LocationRepository repository;
    @Autowired
    private LocationService service;

    @Value("/cms/ms/location")
    private String thisUrl;

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false), new Breadcrumb("Master Location", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", repository.findAll());

        // some part
        model.addAttribute("tableName", "Master Location");
        model.addAttribute("modelName", "location");
        return thisUrl + "/index";
    }

    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(new Breadcrumb("Home", "/", false), new Breadcrumb("Master Location", request.getRequestURI(), true));
        model.addAttribute("breadcrumbs", breadcrumbs);

        // List Field
        List<FieldList> fields = Arrays.asList(
                new FieldList("name", true, false),
                new FieldList("description", false, false),
                new FieldList("address", false, false),
                new FieldList("orders", true, false),
                new FieldList("status", true, false)
        );
        model.addAttribute("fields", fields);

        // some part
        LocationModelDTO.CreateRequest dto = new LocationModelDTO.CreateRequest();
        model.addAttribute("createData", dto);
        model.addAttribute("modelName", "location");
        return thisUrl + "/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("createData") @Valid LocationModelDTO.CreateRequest dto, BindingResult bindingResult, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("createData", dto);
            return thisUrl + "/create";
        }

        service.saveData(dto);
        return "redirect:/cms/ms/location";
    }


}

package com.bca.byc.controller.cms;

import com.bca.byc.entity.StatusType;
import com.bca.byc.model.component.Breadcrumb;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cms/user")
public class UserInquiryController {

    private final UserService service;
    private UserRepository repository;

    // for inquiry
    @GetMapping
    @PreAuthorize("hasPermission(#authentication, 'users.view')")
    public String index(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("Home", "/", false),
                new Breadcrumb("User", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", service.findUserPendingAndActive());

        // some part
        model.addAttribute("titlePage", "Users");
        return "cms/user/inquiry";
    }

    // for active
    @GetMapping("/active_old")
//    @PreAuthorize("hasPermission(#authentication, 'users_active.view')")
    public String active(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("Home", "/", false),
                new Breadcrumb("User Active", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
//        model.addAttribute("datas", repository.findByStatus(StatusType.APPROVE_SPV));
        model.addAttribute("datas", service.findUserActive());

        // some part
        model.addAttribute("titlePage", "Users Active");
        return "cms/user/active";
    }

    // for suspended
    @GetMapping("/suspended_old")
//    @PreAuthorize("hasPermission(#authentication, 'users_active.view')")
    public String suspended(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("Home", "/", false),
                new Breadcrumb("User Suspended", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", repository.findByIsSuspended(true));

        // some part
        model.addAttribute("titlePage", "Users Suspended");
        return "cms/user/suspended";
    }

    // for deleted
    @GetMapping("/deleted_old")
//    @PreAuthorize("hasPermission(#authentication, 'users_active.view')")
    public String deleted(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("Home", "/", false),
                new Breadcrumb("User Deleted", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        //table
        model.addAttribute("datas", repository.findByIsDeleted(true));

        // some part
        model.addAttribute("titlePage", "Users Deleted");

        // get location view
        return "cms/user/deleted";
    }


    // for create users
    @GetMapping("/create")
    @PreAuthorize("hasPermission(#authentication, 'users.view')")
    public String create(Model model, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("User", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);

        // get location view
        return "cms/user/create";
    }

    // for view detail
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#authentication, 'users.view')")
    public String edit(Model model, @PathVariable("id") Long id, HttpServletRequest request) {
        // set breadcrumb
        List<Breadcrumb> breadcrumbs = Arrays.asList(
                new Breadcrumb("User", request.getRequestURI(), true)
        );
        model.addAttribute("breadcrumbs", breadcrumbs);


        // get location view
        model.addAttribute("user", repository.findById(id).get());

        return "cms/user/inquiry_detail";
    }



}
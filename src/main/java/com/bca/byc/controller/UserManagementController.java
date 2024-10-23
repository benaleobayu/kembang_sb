package com.bca.byc.controller;


import com.bca.byc.service.UserActiveService;
import com.bca.byc.service.UserManagementExportService;
import com.bca.byc.service.GlobalAttributeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping(UserManagementController.urlRoute)
@AllArgsConstructor
@Tag(name = "User Management API")
@SecurityRequirement(name = "Authorization")
public class UserManagementController {

    static final String urlRoute = "/cms/v1/um/";
    private final UserActiveService service;
    private final UserManagementExportService exportService;
    private final GlobalAttributeService userManagementService;



}

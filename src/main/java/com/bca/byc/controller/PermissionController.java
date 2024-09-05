package com.bca.byc.controller;

import com.bca.byc.entity.Role;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cms/v1/am/permission")
@Tag(name = "Permission API")
public class PermissionController {

    private PermissionService service;

    @GetMapping
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /cms/v1/am/permission endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found permission", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

}
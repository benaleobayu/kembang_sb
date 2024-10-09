package com.bca.byc.controller;


import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BusinessService;
import com.bca.byc.service.cms.MasterDataExportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(BusinessCmsController.urlRoute)
@Tag(name = "Business API")
@SecurityRequirement(name = "Authorization")
public class BusinessCmsController {

    static final String urlRoute = "/cms/v1/business";
    private final BusinessService service;
    private final MasterDataExportService exportService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<BusinessListResponse>>> listDataBusiness(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PathVariable("userId") String userId) {
        // response true
        log.info("GET " + urlRoute + "/business/{userId} endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list business", service.listDataBusinessUser(pages, limit, sortBy, direction, keyword, userId)));
    }

    @PreAuthorize("hasAuthority('pre-registration.export')")
    @GetMapping("/{userId}/export")
    public void exportExcel(HttpServletResponse response, @PathVariable("userId") String userId) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pre-register.xls";
        response.setHeader(headerKey, headerValue);
        exportService.exportBusinessUser(response, userId);
    }

}
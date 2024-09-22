package com.bca.byc.controller;


import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.bca.byc.controller.PublicController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@Tag(name = "Apps Public API", description = "Public API for public usage")
@AllArgsConstructor
public class PublicController {

    static final String urlRoute = "/api/v1/public";
    private final SettingService settingsService;
    private final FaqCategoryService faqCategoryService;
    private final MsBusinessCategoryService businessCategoryService;
    private final MsLocationService locationService;
    private final ExpectItemService expectItemService;
    private final ExpectCategoryService expectCategoryService;
    private final PreRegisterService preRegisterService;

    // show by identity
    @GetMapping("/setting")
    public ResponseEntity<ApiResponse> showTnc(@RequestParam("identity") String identity) {
        log.info("GET /v1/settings/search?identity={} endpoint hit", identity);
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found settings", settingsService.showByIdentity(identity)));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }


    // how all faq
    @GetMapping("/faq")
    public ResponseEntity<ApiResponse> getAllFaqWithCategory() {
        log.info("GET /api/v1/public/faq endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found Faq ", faqCategoryService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    // business cateogory
    @GetMapping("/business-category")
    public ResponseEntity<ApiResponse> getAllBusinessCategory() {
        log.info("GET /api/v1/public/business_category endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found business category", businessCategoryService.findByParentIdIsNull()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    // location
    @GetMapping("/location")
    public ResponseEntity<ApiResponse> getAllLocation() {
        log.info("GET /api/v1/public/location endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found location", locationService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    // expect category
    @GetMapping("/expect-category")
    public ResponseEntity<ApiResponse> getAllExpectCategory() {
        log.info("GET /api/v1/public/expect-category endpoint hit");
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found expect category", expectCategoryService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    // get test pre register
    @GetMapping("/data-pre-register")
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<PreRegisterDetailResponse>>> listData(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false, defaultValue = "APPROVED") AdminApprovalStatus status,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // response true
        log.info("GET " + urlRoute + " data-pre-register endpoint hit");
        try {
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list pre-register", preRegisterService.listData(pages, limit, sortBy, direction, keyword, status, startDate, endDate), preRegisterService.listStatus()));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
        }
    }

}

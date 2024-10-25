package com.bca.byc.controller;


import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.*;
import com.bca.byc.service.cms.FaqCategoryService;
import com.bca.byc.service.cms.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final BusinessCategoryService businessCategoryService;
    private final LocationService locationService;
    private final ExpectCategoryService expectCategoryService;
    private final PreRegisterService preRegisterService;
    private final PostService postService;
    private final ReasonReportService reasonReportService;

    // show by identity
    @GetMapping("/setting")
    public ResponseEntity<?> showTnc(@RequestParam("identity") String identity) {
        log.info("GET /v1/settings/search?identity={} endpoint hit", identity);
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found settings", settingsService.showByIdentity(identity)));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // how all faq
    @GetMapping("/faq")
    public ResponseEntity<?> getAllFaqWithCategory() {
        log.info("GET /api/v1/public/faq endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found Faq ", faqCategoryService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // business cateogory
    @GetMapping("/business-category")
    public ResponseEntity<?> getAllBusinessCategory() {
        log.info("GET /api/v1/public/business_category endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found business category", businessCategoryService.findByParentIdIsNull()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // location
    @GetMapping("/location")
    public ResponseEntity<?> getAllLocation() {
        log.info("GET /api/v1/public/location endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found location", locationService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // expect category
    @GetMapping("/expect-category")
    public ResponseEntity<?> getAllExpectCategory() {
        log.info("GET /api/v1/public/expect-category endpoint hit");
        try {
            // -- public --
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found expect category", expectCategoryService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // reason report category
    @GetMapping("/reason-report")
    public ResponseEntity<?> ReasonReportList() {
        log.info("GET /api/v1/public/reason-report endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found reason report", reasonReportService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get list post home without bearer", description = "Get list post home")
    @GetMapping("/home-post")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostHomeResponse>>> listDataPostHome(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "isElastic", required = false, defaultValue = "false") Boolean isElastic,
            @Schema(example = "top-picks", description = "top-picks | following | discover")
            @RequestParam(name = "category", required = false, defaultValue = "top-picks") String category) {
        // response true

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", postService.listDataPostHome(null, pages, limit, sortBy, direction, keyword, category, isElastic)));
    }

    // get test pre register
    @GetMapping("/data-pre-register")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PreRegisterDetailResponse>>> listData(
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
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list pre-register", preRegisterService.listData(pages, limit, sortBy, direction, keyword, status, startDate, endDate)));
    }

}

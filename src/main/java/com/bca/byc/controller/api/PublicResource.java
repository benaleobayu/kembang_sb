package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/public")
@Tag(name = "Public API", description = "Public API for public usage")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class PublicResource {

    private final SettingsService settingsService;
    private final FaqCategoryService faqCategoryService;
    private final MsBusinessCategoryService businessCategoryService;
    private final MsLocationService locationService;
    private final ExpectItemService expectItemService;
    private final ExpectCategoryService expectCategoryService;

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

}

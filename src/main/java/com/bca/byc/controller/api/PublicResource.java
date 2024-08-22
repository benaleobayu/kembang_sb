package com.bca.byc.controller.api;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.service.FaqService;
import com.bca.byc.service.SettingsService;
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
    private final FaqService faqService;

    // show by identity
    @GetMapping("/setting")
    public ResponseEntity<ApiListResponse> showTnc(@RequestParam("identity") String identity) {
        log.info("GET /v1/settings/search?identity={} endpoint hit", identity);
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found settings", settingsService.showByIdentity(identity)));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    // show faq
    @GetMapping("/faq")
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /api/v1/faq endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found faq", faqService.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

}

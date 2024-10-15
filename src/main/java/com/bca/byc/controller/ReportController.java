package com.bca.byc.controller;

import com.bca.byc.model.ReportRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bca.byc.controller.ReportController.urlRoute;

@Slf4j
@RestController
@RequestMapping(urlRoute)
@SecurityRequirement(name = "Authorization")
@AllArgsConstructor
@Tag(name = "Report API")
public class ReportController {

    static final String urlRoute = "/api/v1/report";

    private final ReportService service;

    // Report Comment
    @PostMapping("/send-report")
    public ResponseEntity<ApiResponse> sendReport(@Valid @RequestBody ReportRequest dto) {
        log.info("POST " + urlRoute + "/send-report endpoint hit");

        try {
            String message = service.SendReport(dto);
            return ResponseEntity.ok(new ApiResponse(true, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}

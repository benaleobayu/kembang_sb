package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ChangeStatusRequest;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.InputAttributeService;
import com.bca.byc.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ReportCmsController.urlRoute)
@Tag(name = "Report API")
@SecurityRequirement(name = "Authorization")
public class ReportCmsController {

    private final ReportService reportService;
    private final InputAttributeService inputAttributeService;

    static final String urlRoute = "/cms/v1/report";

    @GetMapping("/{id}/detail")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ReportContentIndexResponse>>> ListReportOnDetail(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @Schema(description = "POST | COMMENT | COMMENT_REPLY | USER", example = "POST")
            @RequestParam(name = "detailOf", required = false, defaultValue = "POST" ) String detailOf,
            @PathVariable("id") String reportId
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true,
                "Success get report detail",
                reportService.listReportOnDetail(pages, limit, sortBy, direction, keyword, reportId, detailOf)));
    }

    // update report status
    @Operation(summary = "Update report status", description = "Update report status")
    @PutMapping("/status")
    public ResponseEntity<ApiResponse> updateReportStatus(@RequestBody ChangeStatusRequest dto) {
        log.info("PUT " + urlRoute + " endpoint hit");
        try {
            reportService.updateReportStatus(dto);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully update report status"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}

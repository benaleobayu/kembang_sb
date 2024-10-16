package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ReportCommentDetailResponse;
import com.bca.byc.model.ReportCommentIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportCommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ReportCommentController.urlRoute)
@Tag(name = "Report API")
@SecurityRequirement(name = "Authorization")
public class ReportCommentController {

    static final String urlRoute = "/cms/v1/report/comment";
    private ReportCommentService service;

    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ReportCommentIndexResponse>>> IndexReportComment(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "reportStatus", required = false) String reportStatus,
            @RequestParam(name = "reportType", required = false) String reportType
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok()
                .body(new PaginationCmsResponse<>
                        (true, "Success get list report Comment", service.listDataReportComment(pages,
                                limit,
                                sortBy,
                                direction,
                                keyword,
                                startDate,
                                endDate,
                                reportStatus,
                                reportType)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> DetailReportComment(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            ReportCommentDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found report Comment", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}
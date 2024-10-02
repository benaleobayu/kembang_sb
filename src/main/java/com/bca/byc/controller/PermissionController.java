package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PermissionListResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.bca.byc.controller.PermissionController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(urlRoute)
@Tag(name = "Permission API")
@SecurityRequirement(name = "Authorization")
public class PermissionController {

    private PermissionService service;

    static final String urlRoute = "/cms/v1/am/permission";

    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<?> getAll() {
        log.info("GET /cms/v1/am/permission endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found permission", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(summary = "Get list Permission", description = "Get list Permission")
    @GetMapping("/list")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<PermissionListResponse>>> listDataPermission(
                @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
                @RequestParam(name = "limit", required = false, defaultValue = "1000") Integer limit,
                @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list Permission", service.listDataList(pages, limit, sortBy, direction, keyword)));
        }

}
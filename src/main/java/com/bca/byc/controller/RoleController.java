package com.bca.byc.controller;


import com.bca.byc.model.RoleCreateUpdateRequest;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.model.RoleListResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cms/v1/am/role")
@Tag(name = "Role API")
@SecurityRequirement(name = "Authorization")
public class RoleController {

    private RoleService service;

    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<RoleListResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list role", service.listData(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(hidden = true)
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        log.info("GET /cms/v1/am/role endpoint hit");
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found role", service.findAllData()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        log.info("GET /cms/v1/am/role/{id} endpoint hit");
        try {
            RoleDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found role", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RoleCreateUpdateRequest item) {
        log.info("POST /cms/v1/am/role endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/cms/v1/am/role/"))
                    .body(new ApiResponse(true, "Successfully created role"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody RoleCreateUpdateRequest item) {
        log.info("PUT /cms/v1/am/role/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated role"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /cms/v1/am/role/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted role"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}


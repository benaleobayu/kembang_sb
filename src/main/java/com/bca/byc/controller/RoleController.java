package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RoleDetailResponse;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.model.RoleCreateRequest;
import com.bca.byc.service.RoleService;
import com.bca.byc.model.RoleUpdateRequest;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cms/v1/am/role")
@Tag(name = "CMS Role API")
@SecurityRequirement(name = "Authorization")

public class RoleController {

    private RoleService service;

    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<RoleDetailResponse>>> listFollowUser(
                @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
                @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                @RequestParam(name = "userName", required = false) String userName) {
            // response true
            try{
                return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list role", service.listData(pages, limit, sortBy, direction, userName)));
            }catch (ExpiredJwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
            }
        }

    @GetMapping("/all")
    public ResponseEntity<ApiListResponse> getAll() {
        log.info("GET /cms/v1/am/role endpoint hit");
        try {
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found role", service.findAllData()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiListResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /cms/v1/am/role/{id} endpoint hit");
        try {
            RoleDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiListResponse(true, "Successfully found role", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody RoleCreateRequest item) {
        log.info("POST /cms/v1/am/role endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/cms/v1/am/role/"))
                    .body(new ApiResponse(true, "Successfully created role"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody RoleUpdateRequest item) {
        log.info("PUT /cms/v1/am/role/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated role"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE /cms/v1/am/role/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted role"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}


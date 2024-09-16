package com.bca.byc.controller;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PreRegisterService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.bca.byc.controller.UserPreRegisterController.urlRoute;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(urlRoute)
@Tag(name = "CMS User Pre-Register API")
public class UserPreRegisterController {

    static final String urlRoute = "/cms/v1/um/pre-register";
    private PreRegisterService service;

    @PreAuthorize("hasAuthority('pre-registration.view')")
    @Operation(summary = "Create Pre-Register User", description = "Create Pre-Register User")
    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<PreRegisterDetailResponse>>> listData(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "userName", required = false) String userName) {
        // response true
        log.info("GET " + urlRoute + " list endpoint hit");
        try {
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list pre-register", service.listData(pages, limit, sortBy, direction, userName)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.read')")
    @Operation(summary = "Get detail Pre-Register User by id", description = "Get detail Pre-Register User by id")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            PreRegisterDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found pre-register user", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAuthority('pre-registration.create')")
    @Operation(summary = "Create Pre-Register User", description = "Create Pre-Register User")
    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PreRegisterCreateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        // principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof AppAdmin) {
            email = ((AppAdmin) principal).getEmail();
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid authentication principal."));
        }
        System.out.println(email);

        try {
            service.saveData(item, email);
            return ResponseEntity.created(URI.create("/cms/v1/pre-register/"))
                    .body(new ApiResponse(true, "Successfully created pre-register user"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole()")
    @Operation(summary = "Update Pre-Register User", description = "Update Pre-Register User")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody PreRegisterUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated pre-register user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole()")
    @Operation(summary = "Delete Pre-Register User", description = "Delete Pre-Register User")
    @DeleteMapping()
    public ResponseEntity<ApiResponse> delete(@RequestParam("ids") List<Long> ids) {
        log.info("DELETE " + urlRoute + "/ids={ids} endpoint hit");
        try {
            service.deleteData(ids);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted pre-register user"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
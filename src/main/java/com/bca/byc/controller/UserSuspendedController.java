package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserSuspendedRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserSuspendedService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cms/v1/um/suspended")
@Tag(name = "CMS User Suspended API")
public class UserSuspendedController {

    private UserSuspendedService service;

    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listFollowUser(
                @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
                @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                @RequestParam(name = "userName", required = false) String userName) {
            // response true
            try{
                return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list user", service.listData(pages, limit, sortBy, direction, userName)));
            }catch (ExpiredJwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
            }
        }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET /cms/v1/um/suspended/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found user", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("PATCH /cms/v1/um/suspended/{id}/delete endpoint hit");
        try {
            service.makeUserIsDeletedTrue(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

   @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody UserSuspendedRequest dto) {
        log.info("POST /cms/v1/um/suspended/delete endpoint hit");
        try {
            service.makeUserBulkDeleteTrue(dto.getIds());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ApiResponse> restore(@PathVariable("id") Long id) {
        log.info("PATCH /cms/v1/um/suspended/{id}/restore endpoint hit");
        try {
            service.makeUserIsSuspendedFalse(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<ApiResponse> restore(@RequestBody UserSuspendedRequest dto) {
        log.info("POST /cms/v1/um/suspended/restore endpoint hit");
        try {
            service.makeUserBulkRestoreTrue(dto.getIds());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully restored user", null));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

}
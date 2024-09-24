package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserInquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/cms/v1/user-inquiry")
@Tag(name = "User Inquiry API")
public class UserInquiryController {

    private UserInquiryService service;

    // list
    @Operation(hidden = true)
    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "userName", required = false) String userName) {
        // response true
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list user inquiry", service.listData(pages, limit, sortBy, direction, userName)));
    }


    // create
    @Operation(hidden = true)
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        log.info("GET /cms/v1/user-inquiry/{id} endpoint hit");
        try {
            UserManagementDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found register inquiry", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // soft delete
    @Operation(hidden = true)
    @PatchMapping("{id}/delete")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("PATCH /cms/v1/user-inquiry/{id}/delete endpoint hit");
        try {
            service.softDeleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted register inquiry"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.bca.byc.controller;

import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.cms.CmsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/v1/users")
@RequiredArgsConstructor
public class CmsUserController {

    private final CmsUserService cmsUserService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try{
            return ResponseEntity.ok(new ApiResponse(true, "Users found", cmsUserService.getAllUsers(page, size)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiDataResponse> countAllUsers() {
        try{
            return ResponseEntity.ok(new ApiDataResponse(true, "Users found", cmsUserService.countAllUsers()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }
}

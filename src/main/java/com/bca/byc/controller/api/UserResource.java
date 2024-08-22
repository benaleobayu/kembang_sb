package com.bca.byc.controller.api;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserDetailResponse;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.model.UserUpdatePasswordRequest;
import com.bca.byc.model.UserUpdateRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.ResultPageResponse;
import com.bca.byc.service.UserService;
import com.bca.byc.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "Authorization")
public class UserResource {

    private final UserRepository repository;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiListResponse> getUsers() {
        log.info("GET /api/v1/users endpoint hit");

        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Users found", userService.findAllUsers()));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }

    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<ApiListResponse> getUsersById(@PathVariable("userId") Long userId) {
        log.info("GET /api/v1/users/{} endpoint hit", userId);

        try {
            if (!userService.existsById(userId)) {
                // response invalid
                return ResponseEntity.badRequest().body(new ApiListResponse(false, "User not found", null));
            } else {
                // define
                UserDetailResponse user = userService.findDataById(userId);
                // response true
                return ResponseEntity.ok(new ApiListResponse(true, "User found", user));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserUpdateRequest dto) {
        log.info("PUT /api/v1/users/{}/update endpoint hit", userId);
        try {
            userService.updateData(userId, dto);
            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(hidden = true)
    @PatchMapping("/setpassword")
    public ResponseEntity<ApiResponse> setPassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserSetPasswordRequest dto) {
        log.info("PATCH /api/v1/users/setpassword endpoint hit. Header: {}", authorizationHeader);

        String token = authorizationHeader.replace("Bearer ", "");

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid token"));
        }

        String email = jwtUtil.extractEmailFromToken(token);
        User user = userService.findByEmail(email);
        try {
            // if user not found
            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
            }
            // if user not in status approved
            if (!user.getStatus().equals(StatusType.VERIFIED)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not approved"));
            }
            // if ready condition
            userService.setNewPassword(email, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Password set successfully"));

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable("userId") Long userId, @RequestBody UserUpdatePasswordRequest dto) {
        log.info("PATCH /api/v1/users/{}/password endpoint hit", userId);
        try {
            if (!userService.existsById(userId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
            } else {
                userService.changePassword(userId, dto);
                return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
            }
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // view
    @GetMapping("/list")
    public ResponseEntity<ResultPageResponse<UserDetailResponse>> findDataList(
            @RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
            @RequestParam(name = "userName", required = false) String userName
    ) {
        log.info("GET /api/v1/users/list endpoint hit");

        try {
            // response true
            return ResponseEntity.ok().body(userService.findDataList(pages, limit, sortBy, direction, userName));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(null);
        }
    }
}

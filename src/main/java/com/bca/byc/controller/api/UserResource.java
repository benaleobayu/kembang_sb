package com.bca.byc.controller.api;

import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "User API")
public class UserResource {

    private final UserRepository repository;

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers(){
        return repository.findAll();
    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<ApiListResponse> getUsersById(@PathVariable("userId") Long userId){
        log.info("GET /api/v1/users/{} endpoint hit", userId);

        try{
            if (!userService.existsById(userId)){
                // response invalid
                return ResponseEntity.badRequest().body(new ApiListResponse(false, "User not found", null));
            }else {
                // define
                UserDetailResponse user = userService.findUserById(userId);
                // response true
                return ResponseEntity.ok(new ApiListResponse(true, "User found", user));
            }

        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PatchMapping("/{userId}/setpassword")
    public ResponseEntity<ApiResponse> setPassword(@PathVariable("userId") Long userId, @RequestBody UserSetPasswordRequest dto){
        log.info("PATCH /api/v1/users/{}/setpassword endpoint hit", userId);
        try{
            if (!userService.existsById(userId)){
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
            }else {
                userService.setNewPassword(userId, dto);
                return ResponseEntity.ok(new ApiResponse(true, "Password set successfully"));
            }
        }catch (BadRequestException e){
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
}

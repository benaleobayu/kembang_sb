package com.bca.byc.controller.Api;

import com.bca.byc.entity.User;
import com.bca.byc.model.user.UserUpdatePasswordRequest;
import com.bca.byc.model.user.UserUpdateRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@Tag(name = "User API", description = "User API")
@RequestMapping("/api/test/user")
public class UserExtraController {

    @Autowired
    private final StopWatch stopWatch;
    private final UserRepository repository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getUsers() {
        return repository.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiListResponse> findUserById(@PathVariable("userId") Long userId) {
        stopWatch.start();
        log.debug("Get user request received: {}", userId);

        stopWatch.stop();
        log.info("User found successfully: {} in Execution Time: {}", userId, stopWatch.getTotalTimeMillis());

        return ResponseEntity.ok(new ApiListResponse("success", "Success get data user", userService.getUserDetail(userId)));
    }


    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest dto) {
        stopWatch.start();
        log.debug("Update user request received: {}", userId);

        if (userService.existsById(userId)) {
            stopWatch.stop();
            userService.updateUser(userId, dto);
            log.info("User updated successfully: {}", userId);
            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully."));
        } else {
            stopWatch.stop();
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found."));
        }
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse> patchUser(@PathVariable("userId") Long userId, @RequestBody @Valid UserUpdatePasswordRequest dto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.debug("Patch user request received: {}", userId);

        try {
            if (!userService.existsById(userId)) {
                stopWatch.stop();
                return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found."));
            }

            userService.updateUserPassword(userId, dto);
            stopWatch.stop();
            log.info("User password updated successfully: {} in Execution time: {}", userId, stopWatch.getTotalTimeMillis());
            return ResponseEntity.ok(new ApiResponse(true, "Successfully changed password."));
        } catch (RuntimeException e) {
            stopWatch.stop();
            log.error("Failed to update password for user {}. Execution time: {} ms", userId, stopWatch.getTotalTimeMillis(), e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}

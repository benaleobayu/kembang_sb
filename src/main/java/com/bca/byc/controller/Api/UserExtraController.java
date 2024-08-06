package com.bca.byc.controller.Api;

import com.bca.byc.model.user.UserUpdateRequest;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/test/user")
public class UserExtraController {

    @Autowired
    private final StopWatch stopWatch;

    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiListResponse> findUserById(@PathVariable("userId") Long userId){
        stopWatch.start();
        log.debug("Get user request received: {}", userId);

        if (userService.existsById(userId)) {
            stopWatch.stop();
            return ResponseEntity.ok(new ApiListResponse("success","Success get data user", userService.getUserDetail(userId)));
        } else {
            stopWatch.stop();
            return ResponseEntity.badRequest().body(new ApiListResponse("error", "User not found.", null));
        }
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
}

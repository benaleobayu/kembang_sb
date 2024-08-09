package com.bca.byc.controller.api;

import com.bca.byc.model.cms.FeedbackCategoryCreateRequest;
import com.bca.byc.model.cms.FeedbackCategoryUpdateRequest;
import com.bca.byc.repository.FeedbackCategoryRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.FeedbackCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/feedback_category")
@Tag(name = "Feedback_Category API")
public class FeedbackCategoryResource {

    private final FeedbackCategoryService service;

    private final FeedbackCategoryRepository repository;

    @GetMapping
    public ResponseEntity<ApiListResponse> getFeedbackesCategory() {
        log.info("GET /api/v1/feedback endpoint hit");

        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Feedbackes found", service.findAllData()));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createFeedbackCategory(@Valid @RequestBody FeedbackCategoryCreateRequest dto) {
        log.info("POST /api/v1/feedback endpoint hit");
        try {
            // save
            service.saveData(dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Feedback created successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{feedbackId}/details")
    public ResponseEntity<ApiListResponse> getFeedbackCategory(@PathVariable("feedbackId") Long feedbackId) {
        log.info("GET /api/v1/feedback/{}/details endpoint hit", feedbackId);
        try {
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Feedback found", service.findDataById(feedbackId)));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{feedbackId}/update")
    public ResponseEntity<ApiResponse> updateFeedbackCategory(@PathVariable("feedbackId") Long feedbackId, @Valid @RequestBody FeedbackCategoryUpdateRequest dto) {
        log.info("PUT /api/v1/feedback/{}/update endpoint hit", feedbackId);

        try {
            if (!repository.existsById(feedbackId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Feedback not found"));
            }
            // update
            service.updateData(feedbackId, dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Feedback updated successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{feedbackId}/delete")
    public ResponseEntity<ApiResponse> deleteFeedbackCategory(@PathVariable("feedbackId") Long feedbackId) {
        log.info("DELETE /api/v1/feedback/{}/delete endpoint hit", feedbackId);
        try {
            if (!repository.existsById(feedbackId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Feedback not found"));
            }
            // delete
            service.deleteData(feedbackId);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Feedback deleted successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}

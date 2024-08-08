package com.bca.byc.controller.api;

import com.bca.byc.model.api.InterestCreateRequest;
import com.bca.byc.model.api.InterestUpdateRequest;
import com.bca.byc.repository.InterestRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.InterestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/interest")
@Tag(name = "Interest API")
public class InterestResource {

    private final InterestService interestService;

    private final InterestRepository repository;

    @GetMapping
    public ResponseEntity<ApiListResponse> getInterests(){
        log.info("GET /api/v1/interest endpoint hit");

        try{
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Interestes found", interestService.getAllData()));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createInterest(@RequestBody InterestCreateRequest dto){
        log.info("POST /api/v1/interest endpoint hit");
        try{
            // save
            interestService.saveData(dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest created successfully"));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{interestId}/details")
    public ResponseEntity<ApiListResponse> getInterest(@PathVariable("interestId") Long interestId) {
        log.info("GET /api/v1/interest/{}/details endpoint hit", interestId);
        try{
            // response true
            return ResponseEntity.ok(new ApiListResponse(true, "Interest found", interestService.findDataById(interestId)));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiListResponse(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{interestId}/update")
    public ResponseEntity<ApiResponse> updateInterest(@PathVariable("interestId") Long interestId, @Valid @RequestBody InterestUpdateRequest dto) {
        log.info("PUT /api/v1/interest/{}/update endpoint hit", interestId);

        try{
            if (!repository.existsById(interestId)){
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Interest not found"));
            }
            // update
            interestService.updateData(interestId, dto);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest updated successfully"));
        }catch (Exception e){
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }

    }

    @DeleteMapping("/{interestId}/delete")
    public ResponseEntity<ApiResponse> deleteInterest(@PathVariable("interestId") Long interestId) {
        log.info("DELETE /api/v1/interest/{}/delete endpoint hit", interestId);
        try {
            if (!repository.existsById(interestId)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Interest not found"));
            }
            // delete
            interestService.deleteData(interestId);
            // response success
            return ResponseEntity.ok(new ApiResponse(true, "Interest deleted successfully"));
        } catch (Exception e) {
            // response error
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

}

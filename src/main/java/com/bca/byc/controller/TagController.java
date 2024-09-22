package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.data.TagCreateUpdateRequest;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(TagController.urlRoute)
@Tag(name = "Tag API")
public class TagController {

    static final String urlRoute = "/cms/v1/ms/tag";
    private TagService service;

    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<TagDetailResponse>>> listDataTag(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        try{
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list tag detail", service.listDataTag(pages, limit, sortBy, direction, keyword)));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            TagDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found tag", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody TagCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(item);
            return ResponseEntity.created(URI.create("/cms/v1/ms/tag/"))
                    .body(new ApiResponse(true, "Successfully created tag"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @Valid @RequestBody TagCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(id, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated tag"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted tag"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
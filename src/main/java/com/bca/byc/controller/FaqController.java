package com.bca.byc.controller;


import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.FaqService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(FaqController.urlRoute)
@Tag(name = "Faq-Items API [Masterdata]")
@SecurityRequirement(name = "Authorization")
public class FaqController {

    static final String urlRoute = "/cms/v1/ms/faq";
    private final FaqService service;

    @PreAuthorize("hasAuthority('faq.view')")
    @GetMapping("/{categoryId}/items")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<FaqIndexResponse>>> listDataFaq(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PathVariable("categoryId") String categoryId) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list faq", service.listDataFaq(pages, limit, sortBy, direction, keyword, categoryId)));
    }

    @GetMapping("/{categoryId}/items/{itemId}")
    public ResponseEntity<?> getById(@PathVariable("itemId") String itemId, @PathVariable("categoryId") String categoryId) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            FaqDetailResponse item = service.findDataById(categoryId, itemId);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found faq", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/{categoryId}/items")
    public ResponseEntity<ApiResponse> create(@PathVariable("categoryId") String categoryId ,@Valid @RequestBody FaqCreateUpdateRequest item) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(categoryId, item);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{categoryId}/items/{itemId}")
    public ResponseEntity<ApiResponse> update(@PathVariable("categoryId") String categoryId, @PathVariable("itemId") String itemId, @Valid @RequestBody FaqCreateUpdateRequest item) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.updateData(categoryId, itemId, item);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{categoryId}/items/{itemId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("categoryId") String categoryId, @PathVariable("itemId") String itemId) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(categoryId, itemId);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted faq"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
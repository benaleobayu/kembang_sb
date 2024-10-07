package com.bca.byc.controller;

import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.model.search.SearchResultAccountResponse;
import com.bca.byc.model.search.SearchResultPostResponse;
import com.bca.byc.model.search.SearchResultTagResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(AppSearchController.urlRoute)
@AllArgsConstructor
@Tag(name = "Apps Data API")
@SecurityRequirement(name = "Authorization")
public class AppSearchController {

    static final String urlRoute = "/api/v1/data";

    private final AppSearchService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/suggested")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<OnboardingListUserResponse>>> listSuggested(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "6") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        log.info("GET " + urlRoute + "/suggested endpoint hit");
        // response true
        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list suggested user", service.listSuggestedUser(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get list result posts", description = "Get list result posts")
    @GetMapping("/post")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<SearchResultPostResponse>>> listResultPosts(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", service.listResultPosts(email, pages, limit, sortBy, direction, tag)));
    }

    @Operation(summary = "Get list result tags", description = "Get list result tags", hidden = true)
    @GetMapping("/result/tag")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<SearchResultTagResponse>>> listResultTags(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", service.listResultTags(email, pages, limit, sortBy, direction, tag)));
    }

    @Operation(summary = "Get list result accounts", description = "Get list result accounts", hidden = true)
    @GetMapping("/result/account")
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<SearchResultAccountResponse>>> listResultAccounts(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", service.listResultAccounts(email, pages, limit, sortBy, direction, tag)));
    }


}

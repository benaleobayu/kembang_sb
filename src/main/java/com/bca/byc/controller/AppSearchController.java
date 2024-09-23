package com.bca.byc.controller;

import com.bca.byc.model.search.SearchResultAccountResponse;
import com.bca.byc.model.search.SearchResultPostResponse;
import com.bca.byc.model.search.SearchResultTagResponse;
import com.bca.byc.response.AppPaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppSearchService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app-search")
@AllArgsConstructor
@Tag(name = "App Search API")
public class AppSearchController {

    private final AppSearchService service;

    @GetMapping("/result/post")
    public ResponseEntity<AppPaginationResponse<ResultPageResponseDTO<SearchResultPostResponse>>> listResultPosts(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new AppPaginationResponse<>(true, "Success get list post", service.listResultPosts(email, pages, limit, sortBy, direction, tag)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AppPaginationResponse<>(false, "Unauthorized", null));
        }
    }
   @GetMapping("/result/tag")
    public ResponseEntity<AppPaginationResponse<ResultPageResponseDTO<SearchResultTagResponse>>> listResultTags(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new AppPaginationResponse<>(true, "Success get list post", service.listResultTags(email, pages, limit, sortBy, direction, tag)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AppPaginationResponse<>(false, "Unauthorized", null));
        }
    }
   @GetMapping("/result/account")
    public ResponseEntity<AppPaginationResponse<ResultPageResponseDTO<SearchResultAccountResponse>>> listResultAccounts(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new AppPaginationResponse<>(true, "Success get list post", service.listResultAccounts(email, pages, limit, sortBy, direction, tag)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AppPaginationResponse<>(false, "Unauthorized", null));
        }
    }



}

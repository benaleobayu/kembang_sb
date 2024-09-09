package com.bca.byc.controller;

import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.response.PaginationResponse;
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

    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<PostDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "categories", required = false, defaultValue = "posts") String categories) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list post", service.listData(email, pages, limit, sortBy, direction, tag, categories)));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
        }
    }

}

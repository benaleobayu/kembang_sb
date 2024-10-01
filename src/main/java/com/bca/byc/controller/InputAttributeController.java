package com.bca.byc.controller;


import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.InputAttributeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(InputAttributeController.urlRoute)
@Tag(name = "Input Attribute API")
@SecurityRequirement(name = "Authorization")
public class InputAttributeController {

    static final String urlRoute = "/cms/v1/attribute";
    private InputAttributeService service;

    @GetMapping("subcategory-business")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AttributeResponse>>> listDataAttributeList(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list attribute", service.listDataSubCategoryBusiness(pages, limit, sortBy, direction, keyword)));
    }

    @GetMapping("subcategory-expect")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AttributeResponse>>> listDataSubExpect(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list attribute", service.listDataSubCategoryExpect(pages, limit, sortBy, direction, keyword)));
    }



}
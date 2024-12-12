package com.kembang.controller;


import com.kembang.model.attribute.AttributeResponse;
import com.kembang.response.PaginationCmsResponse;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.InputAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(InputAttributeController.urlRoute)
@Tag(name = "Input Attribute API")
@SecurityRequirement(name = "Authorization")
public class InputAttributeController {

    static final String urlRoute = "/cms/v1/attribute";
    private InputAttributeService service;

    @GetMapping("location")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AttributeResponse<Long>>>> listDataLocation(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list attribute", service.listDataLocation(pages, limit, sortBy, direction, keyword)));
    }

  @GetMapping("role")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AttributeResponse<Long>>>> RoleList(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list attribute role", service.RoleList(pages, limit, sortBy, direction, keyword)));
    }

}
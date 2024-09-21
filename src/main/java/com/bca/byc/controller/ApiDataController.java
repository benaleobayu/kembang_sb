package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.TagService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ApiDataController.urlRoute)
@Tag(name = "Data API")
@SecurityRequirement(name = "Authorization")
public class ApiDataController {

    static final String urlRoute = "/api/v1/data";

    private final TagService tagService;

   @GetMapping
   public ResponseEntity<PaginationResponse<ResultPageResponseDTO<TagDetailResponse>>> listDataTag(
               @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
               @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
               @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
               @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
               @RequestParam(name = "keyword", required = false) String keyword) {
           // response true
           try{
               return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list tag detail", tagService.listDataTag(pages, limit, sortBy, direction, keyword)));
           }catch (Exception e) {
               return ResponseEntity.badRequest().body(new PaginationResponse<>(false, e.getMessage(), null));
           }
       }


}
package com.bca.byc.controller;


import com.bca.byc.converter.parsing.TreeChannel;
import com.bca.byc.entity.Channel;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ChanelDetailResponse;
import com.bca.byc.model.ChanelIndexResponse;
import com.bca.byc.model.ChanelListContentResponse;
import com.bca.byc.model.ChannelCreateUpdateRequest;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.ChannelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ChannelController.urlRoute)
@Tag(name = "Channel API")
@SecurityRequirement(name = "Authorization")
public class ChannelController {

    static final String urlRoute = "/cms/v1/channel";
    private ChannelService service;

    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ChanelIndexResponse>>> listDataChanelIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list chanel", service.listDataChanelIndex(pages, limit, sortBy, direction, keyword)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            ChanelDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found chanel", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @ModelAttribute(value = "logo") MultipartFile logo,
            @ModelAttribute(value = "name") String name,
            @ModelAttribute(value = "orders") Integer orders,
            @ModelAttribute(value = "description") String description,
            @ModelAttribute(value = "status") Boolean status,
            @ModelAttribute(value = "privacy") String privacy
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            Channel data = TreeChannel.savedChannel(name, orders, logo, description, privacy, status);
            service.saveData(data, logo);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created chanel"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to upload image"));
        }
    }

    @PutMapping(value = "{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @ModelAttribute(value = "logo") MultipartFile logo,
            @ModelAttribute(value = "name") String name,
            @ModelAttribute(value = "orders") Integer orders,
            @ModelAttribute(value = "description") String description,
            @ModelAttribute(value = "status") Boolean status,
            @ModelAttribute(value = "privacy") String privacy
    ) {
        log.info("PUT " + urlRoute + " endpoint hit");
        try {
            ChannelCreateUpdateRequest data = TreeChannel.ChannelPartDTO(name, orders, logo, description, privacy, status);
            service.updateData(id, data);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created chanel"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Failed to upload image"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted chanel"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // ------------------------------------------------------------------

    @GetMapping("/{chennelId}/content")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ChanelListContentResponse<Long>>>> listDataContentIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list chanel", service.listDataContentChannel(pages, limit, sortBy, direction, keyword)));
    }
}

package com.bca.byc.controller;


import com.bca.byc.converter.parsing.TreeChannel;
import com.bca.byc.entity.Channel;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.GlobalAttributeService;
import com.bca.byc.service.cms.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.time.LocalDate;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ChannelController.urlRoute)
@Tag(name = "Channel API")
@SecurityRequirement(name = "Authorization")
public class ChannelController {

    static final String urlRoute = "/cms/v1/channel";
    private ChannelService service;
    private final GlobalAttributeService attributeService;

    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ChanelIndexResponse>>> listDataChanelIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "orders") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) Boolean status,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "export", required = false) Boolean export // TODO export channel
    ) {
        // response true
        CompilerFilterRequest filter = new CompilerFilterRequest(
                pages, limit, sortBy, direction, keyword,
                startDate, endDate, status
        );
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list chanel", service.listDataChanelIndex(filter), attributeService.listAttributeChannel()));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            ChanelDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found chanel", item));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> create(
            @RequestPart(value = "logo") MultipartFile logo,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "orders") Integer orders,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "status") Boolean status,
            @RequestParam(value = "privacy") String privacy
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            Channel data = TreeChannel.savedChannel(name, orders, logo, description, privacy, status);
            service.saveData(data, logo);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created chanel"));
        } catch (BadRequestException | IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping(value = "{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "orders") Integer orders,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "status") Boolean status,
            @RequestParam(value = "privacy") String privacy
    ) {
        log.info("PUT " + urlRoute + " endpoint hit");
        try {
            ChannelCreateUpdateRequest data = TreeChannel.ChannelPartDTO(name, orders, logo, description, privacy, status);
            service.updateData(id, data);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created chanel"));
        } catch (BadRequestException | IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Update status channel", hidden = true)
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id
    ) {
        log.info("PUT " + urlRoute + " endpoint hit");
        try {
            service.updateStatusChannel(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully update status chanel"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Update status channel")
    @GetMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("id") String id,
            @RequestParam("isActive") Boolean status
    ) {
        log.info("PUT " + urlRoute + " endpoint hit");
        try {
            ReturnStatusResponse message = service.getUpdateStatusChannel(id, status);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully update status chanel", message));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted chanel"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // ------------------------------------------------------------------

    @GetMapping("/{channelId}/content")
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<ChanelListContentResponse<Long>>>> listDataContentIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @PathVariable(name = "channelId", required = false) String channelId
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list chanel", service.listDataContentChannel(pages, limit, sortBy, direction, keyword, channelId)));
    }
}

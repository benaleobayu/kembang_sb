package com.bca.byc.controller;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AccountCreateUpdateRequest;
import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.response.*;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AccountController.urlRoute)
@Tag(name = "Account API")
@SecurityRequirement(name = "Authorization")
public class AccountController {

    static final String urlRoute = "/cms/v1/accounts";
    private AccountService service;

    @Operation(summary = "Get List Account", description = "Get List Account")
    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AccountIndexResponse>>> listDataAccountIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "export", required = false) Boolean export // TODO export account
            ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list accounts", service.listDataAccountIndex(pages, limit, sortBy, direction, keyword)));
    }

    @Operation(summary = "Get Detail Account", description = "Get Detail Account")
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            AccountDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found accounts", item));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Create Account", description = "Create Account")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> create(
            @RequestPart("avatar") MultipartFile avatar,
            @RequestPart("cover") MultipartFile cover,
            @RequestParam("name") String name,
            @RequestParam("channelIds") Set<String> channelIds,
            @RequestParam("status") Boolean status
    ) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.saveData(avatar, cover, name, status, channelIds);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created accounts"));
        } catch (BadRequestException | IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @SneakyThrows
    @Operation(summary = "Update Account", description = "Update Account")
    @PutMapping(value = "{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("id") String id,
            @RequestPart(name = "avatar", required = false) MultipartFile avatar,
            @RequestPart(name = "cover", required = false) MultipartFile cover,
            @RequestParam("name") String name,
            @RequestParam("channelIds") Set<String> channelIds,
            @RequestParam("status") Boolean status
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
            service.updateData(id, avatar, cover, name, channelIds, status);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated accounts"));
    }

    @Operation(summary = "Delete Account", description = "Delete Account")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted accounts"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
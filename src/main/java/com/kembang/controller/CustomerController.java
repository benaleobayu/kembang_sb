package com.kembang.controller;

import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.Documents;
import com.kembang.exception.BadRequestException;
import com.kembang.model.CustomerCreateUpdateRequest;
import com.kembang.model.CustomerDetailResponse;
import com.kembang.model.CustomerIndexResponse;
import com.kembang.response.ApiDataResponse;
import com.kembang.response.ApiResponse;
import com.kembang.response.PaginationCmsResponse;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.CustomerService;
import com.kembang.service.DocumentsService;
import com.kembang.service.GlobalAttributeService;
import com.kembang.service.ImportService;
import com.kembang.util.FileUploadHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(CustomerController.urlRoute)
@Tag(name = "Customer API")
@SecurityRequirement(name = "Authorization")
public class CustomerController {

    static final String urlRoute = "/cms/v1/customers";

    private final GlobalAttributeService attributeService;
    private final ImportService importService;
    private final CustomerService service;
    private final DocumentsService documentsService;

    @Value("${app.dev}")
    private Boolean isDev;

    @Value("${app.base.url}")
    private String baseUrl;

    //    @PreAuthorize("hasAuthority('customer.view')")
    @Operation(summary = "Get List Customer", description = "Get List Customer")
    @GetMapping
    public ResponseEntity<?> CustomerIndex(@RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                           @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
                                           @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                                           @RequestParam(name = "keyword", required = false) String keyword,
                                           @RequestParam(name = "location", required = false) String location,
                                           @RequestParam(name = "isSubscriber", required = false) Boolean isSubscriber,
                                           @RequestParam(name = "export", required = false) Boolean export
    ) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        try {
            ResultPageResponseDTO<CustomerIndexResponse> dto = service.ListCustomerIndex(pages, limit, sortBy, direction, keyword, location, isSubscriber, export);
            List<Map<String, List<?>>> attribute = attributeService.listAttributeCustomer();
            return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list user", dto, attribute));
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    //    @PreAuthorize("hasAuthority('customer.view')")
    @Operation(summary = "Get Customer Detail", description = "Get Customer Detail")
    @GetMapping("{id}")
    public ResponseEntity<?> FindCustomerById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            CustomerDetailResponse item = service.FindCustomerById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found customer", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    //    @PreAuthorize("hasAuthority('customer.create')")
    @Operation(summary = "Create Customer", description = "Create Customer")
    @PostMapping
    public ResponseEntity<ApiResponse> CreateCustomer(@Valid @RequestBody CustomerCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.CreateCustomer(dto);
            return ResponseEntity.created(URI.create("/cms/v1/am/customer/")).body(new ApiResponse(true, "Successfully created customer"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    //    @PreAuthorize("hasAuthority('customer.update')")
    @Operation(summary = "Update Customer", description = "Update Customer")
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> UpdateCustomer(@PathVariable("id") String id, @Valid @RequestBody CustomerCreateUpdateRequest dto) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            service.UpdateCustomer(id, dto);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated customer"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


    //    @PreAuthorize("hasAuthority('customer.delete')")
    @Operation(summary = "Delete Customer", description = "Delete Customer")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> DeleteCustomer(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.DeleteCustomer(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted customer"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // import customer
    @Operation(summary = "Import Customer", description = "Import Customer")
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importCustomer(
            @RequestParam("file") MultipartFile file,
            @RequestParam("isDownloadSample") Boolean isDownloadSample
    ) {
        log.info("POST " + urlRoute + "/import endpoint hit");
        if (Boolean.TRUE.equals(isDownloadSample)) {
            try {
                String identity = "SAMPLE_CUSTOMER";
                Documents documents = documentsService.findByIdentity(identity.toLowerCase())
                        .orElseThrow(() -> new BadRequestException("SAMPLE_CUSTOMER not found"));
                String urlFile = GlobalConverter.getParseImage(documents.getUrlFile(), baseUrl);
                String filename = documents.getName() + ".xlsx";
                FileUploadHelper.downloadFileFromUrl(urlFile, filename);
                return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully get link download", urlFile));
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        } else {
            if (file.isEmpty() || file == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "File is empty"));
            }
            try {
                importService.CustomerImport(file);
                return ResponseEntity.ok(new ApiResponse(true, "Successfully import customer"));
            } catch (Exception e) {
                if (isDev) {
                    log.error("Error {}", e.getMessage(), e);
                }
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}

package com.bca.byc.controller;

import com.bca.byc.model.CustomerCreateUpdateRequest;
import com.bca.byc.model.CustomerDetailResponse;
import com.bca.byc.model.CustomerIndexResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.CustomerService;
import com.bca.byc.service.GlobalAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private final CustomerService service;

    @PreAuthorize("hasAuthority('customer.view')")
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
        ResultPageResponseDTO<CustomerIndexResponse> dto = service.ListCustomerIndex(pages, limit, sortBy, direction, keyword, location, isSubscriber, export);
        List<Map<String, List<?>>> attribute = attributeService.listAttributeCustomer();
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list user", dto, attributeService.listAttributeRole()));

    }

    @PreAuthorize("hasAuthority('customer.view')")
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

    @PreAuthorize("hasAuthority('customer.create')")
    @Operation(summary = "Create Customer", description = "Create Customer")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> CreateCustomer(@Valid @RequestBody CustomerCreateUpdateRequest dto) {
        log.info("POST " + urlRoute + " endpoint hit");
        try {
            service.CreateCustomer(dto);
            return ResponseEntity.created(URI.create("/cms/v1/am/customer/")).body(new ApiResponse(true, "Successfully created customer"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('customer.update')")
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


    @PreAuthorize("hasAuthority('customer.delete')")
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
}

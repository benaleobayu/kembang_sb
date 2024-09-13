package com.bca.byc.controller;

import com.bca.byc.model.Elastic.AppUserElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.Page;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.CmsUserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/v1/users")
public class CmsUserController {

    private final CmsUserService cmsUserService;

    public CmsUserController(CmsUserService cmsUserService) {
        this.cmsUserService = cmsUserService;
    }

    // elastic search
    @GetMapping("/list")
    public Page<AppUserElastic> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return cmsUserService.getAllUsers(page, size);
    }

    // postgres
    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<UserManagementDetailResponse>>> listFollowUser(
                @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
                @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                @RequestParam(name = "userName", required = false) String userName) {
            // response true
            try{
                return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list User", cmsUserService.listData(pages, limit, sortBy, direction, userName)));
            }catch (ExpiredJwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PaginationResponse<>(false, "Unauthorized", null));
            }
        }

    @GetMapping("/count")
    public ResponseEntity<ApiDataResponse> countAllUsers() {
        try{
            return ResponseEntity.ok(new ApiDataResponse(true, "Users found", cmsUserService.countAllUsers()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiDataResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/elastic/count")
    private Long getCountByElastic(){
        return cmsUserService.getElasticCount();
    }

}

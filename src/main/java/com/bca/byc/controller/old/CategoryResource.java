package com.bca.byc.controller.old;

import java.net.URI;

import com.bca.byc.response.ApiDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bca.byc.model.old.CategoryCreateUpdateRecordDTO;
import com.bca.byc.model.old.CategoryListResponseDTO;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.old.CategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class CategoryResource {

	private final CategoryService categoryService;

	@Operation(hidden = true)
	@PreAuthorize("hasAuthority('admin.create')")
	@PostMapping("/v1/category")
	public ResponseEntity<Void> createAndUpdateCategory(@RequestBody @Valid CategoryCreateUpdateRecordDTO dto){
		categoryService.createAndUpdateCategory(dto);
		return ResponseEntity.created(URI.create("/v1/category")).build();
		
	}

	@Operation(hidden = true)
	@PreAuthorize("hasAuthority('admin.view')")
	@GetMapping("/v1/category")
	public ResponseEntity<ApiDataResponse<ResultPageResponseDTO<CategoryListResponseDTO>>> findCategoryList(
			@RequestParam(name = "pages", required = true, defaultValue = "0") Integer pages, 
			@RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
			@RequestParam(name="sortBy", required = true, defaultValue = "name") String sortBy,
			@RequestParam(name="direction", required = true, defaultValue = "asc") String direction,
			@RequestParam(name="categoryName", required = false) String categoryName){
		try{
			return ResponseEntity.ok().body( new ApiDataResponse<>(true, "success", categoryService.findCategoryList(
					pages, limit, sortBy, direction, categoryName)) );
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}

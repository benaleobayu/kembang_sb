package com.bca.byc.service.old.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bca.byc.entity.old.Category;
import com.bca.byc.model.old.CategoryCreateUpdateRecordDTO;
import com.bca.byc.model.old.CategoryListResponseDTO;
import com.bca.byc.model.old.CategoryQueryDTO;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.repository.old.CategoryRepository;
import com.bca.byc.service.old.CategoryService;
import com.bca.byc.util.PaginationUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepository categoryRepository;

	@Override
	public void createAndUpdateCategory(CategoryCreateUpdateRecordDTO dto) {
		 Category category =  categoryRepository.findByCode(dto.code().toLowerCase()).orElse(new Category());
		 if(category.getCode()==null) {
			 category.setCode(dto.code().toLowerCase()); //new 
		 }
		 category.setName(dto.name());
		 category.setDescription(dto.description());
		 
		 categoryRepository.save(category);
	}

	@Override
	public ResultPageResponseDTO<CategoryListResponseDTO> findCategoryList(Integer pages, Integer limit, String sortBy,
			String direction, String categoryName) {
		categoryName =  StringUtils.isEmpty(categoryName) ? "%":categoryName+"%";
		Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
		Pageable pageable = PageRequest.of(pages, limit, sort);
		Page<Category> pageResult =  categoryRepository.findByNameLikeIgnoreCase(categoryName, pageable);
		List<CategoryListResponseDTO> dtos =  pageResult.stream().map((c)->{
			CategoryListResponseDTO dto = new CategoryListResponseDTO();
			dto.setCode(c.getCode());
			dto.setName(c.getName());
			dto.setDescription(c.getDescription());
			return dto;
		}).collect(Collectors.toList());

		int currentPage = pageResult.getNumber() + 1;
		int totalPages = pageResult.getTotalPages();

		return PaginationUtil.createResultPageDTO(
				pageResult.getTotalElements(), // total items
				dtos,
				currentPage, // current page
				currentPage > 1 ? currentPage - 1 : 1, // prev page
				currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
				1, // first page
				totalPages - 1, // last page
				pageResult.getSize() // per page
		);
	}

	@Override
	public List<Category> findCategories(List<String> categoryCodeList) {
		List<Category> categories= categoryRepository.findByCodeIn(categoryCodeList);
		if(categories.isEmpty()) throw new BadRequestException("category cant empty");
		return categories;
	}

	@Override
	public List<CategoryListResponseDTO> constructDTO(List<Category> categories) {
		return categories.stream().map((c)->{
			CategoryListResponseDTO dto = new CategoryListResponseDTO();
			dto.setCode(c.getCode());
			dto.setName(c.getName());
			dto.setDescription(c.getDescription());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Map<Long, List<String>> findCategoriesMap(List<Long> bookIdList) {
		List<CategoryQueryDTO> queryList =  categoryRepository.findCategoryByBookIdList(bookIdList);
		Map<Long, List<String>> categoryMaps = new HashMap<>();
		List<String> categoryCodeList = null;
		for(CategoryQueryDTO q:queryList) {
			if(!categoryMaps.containsKey(q.getBookId())) {
				categoryCodeList = new ArrayList<>();
			}else {
				categoryCodeList = categoryMaps.get(q.getBookId());
			}
			categoryCodeList.add(q.getCategoryCode());
			categoryMaps.put(q.getBookId(), categoryCodeList);
		}
		return categoryMaps;
	}

}

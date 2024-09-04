package com.bca.byc.service.old;

import java.util.List;
import java.util.Map;

import com.bca.byc.entity.old.Category;
import com.bca.byc.model.old.CategoryCreateUpdateRecordDTO;
import com.bca.byc.model.old.CategoryListResponseDTO;
import com.bca.byc.response.ResultPageResponseDTO;

public interface CategoryService {
	
	public void createAndUpdateCategory(CategoryCreateUpdateRecordDTO dto);
	
	public ResultPageResponseDTO<CategoryListResponseDTO> findCategoryList(Integer pages, 
			Integer limit, String sortBy, String direction, String categoryName);
	
	public List<Category> findCategories(List<String> categoryCodeList);
	
	public List<CategoryListResponseDTO> constructDTO(List<Category> categories);
	
	public Map<Long, List<String>> findCategoriesMap(List<Long> bookIdList);
	
}

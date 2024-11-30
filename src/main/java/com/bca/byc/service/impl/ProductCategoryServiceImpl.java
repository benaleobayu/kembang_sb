package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.ProductCategory;
import com.bca.byc.model.CompilerFilterRequest;
import com.bca.byc.model.SimpleCmsResponse;
import com.bca.byc.model.SimpleCreateUpdateRequest;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ProductCategoryRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ProductCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final AppAdminRepository adminRepository;

    private final ProductCategoryRepository businessPositionRepository;

    @Override
    public ResultPageResponseDTO<SimpleCmsResponse> listDataProductCategory(CompilerFilterRequest f) {
        ListOfFilterPagination filter = new ListOfFilterPagination(f.keyword());
        SavedKeywordAndPageable set = GlobalConverter.createPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), f.keyword(), filter);

        Page<ProductCategory> pageResult = businessPositionRepository.listDataProductCategory(set.keyword(), set.pageable());
        List<SimpleCmsResponse> dtos = pageResult.stream().map((c) -> {
            SimpleCmsResponse dto = new SimpleCmsResponse();
            dto.setName(c.getName());
            dto.setIsActive(c.getIsActive());
            GlobalConverter.CmsIDTimeStampResponseAndId(dto, c, adminRepository);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public SimpleCmsResponse findDataBySecureId(String id) {
        ProductCategory data = HandlerRepository.getEntityBySecureId(id, businessPositionRepository, "Business position not found");
        SimpleCmsResponse dto = new SimpleCmsResponse();
        dto.setName(data.getName());
        dto.setIsActive(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data, adminRepository);
        return dto;
    }

    @Override
    public void saveData(SimpleCreateUpdateRequest dto) {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        ProductCategory data = new ProductCategory();
        data.setName(dto.getName());
        data.setIsActive(dto.getIsActive());
        GlobalConverter.CmsAdminCreateAtBy(data, adminLogin.getId());
        businessPositionRepository.save(data);
    }

    @Override
    public void updateData(String id, SimpleCreateUpdateRequest dto) {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        ProductCategory data = HandlerRepository.getEntityBySecureId(id, businessPositionRepository, "Business position not found");
        data.setName(dto.getName());
        data.setIsActive(dto.getIsActive());
        GlobalConverter.CmsAdminUpdateAtBy(data, adminLogin.getId());
        businessPositionRepository.save(data);
    }

    @Override
    public void deleteData(String id) {
        ProductCategory data = HandlerRepository.getEntityBySecureId(id, businessPositionRepository, "Business position not found");
        businessPositionRepository.delete(data);
    }
}

package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.ProductCategory;
import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.SimpleCmsResponse;
import com.kembang.model.SimpleCreateUpdateRequest;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.ProductCategoryRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.ProductCategoryService;
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

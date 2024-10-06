package com.bca.byc.service;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCatalog;
import com.bca.byc.model.BusinessCatalogCountsResponse;
import com.bca.byc.model.BusinessCatalogDetailResponse;
import com.bca.byc.model.BusinessCatalogListResponse;
import com.bca.byc.repository.BusinessCatalogRepository;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessCatalogService {

    private final AppUserRepository appUserRepository;
    private final BusinessCatalogRepository businessCatalogRepository;
    private final BusinessRepository businessRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    public ResultPageResponseDTO<BusinessCatalogListResponse> listDataBusinessCatalog(String businessId, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        Business business = HandlerRepository.getEntityBySecureId(businessId, businessRepository, "Business not found");

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<BusinessCatalog> pageResult = businessCatalogRepository.findByTitleLikeIgnoreCase(business.getId(), keyword, pageable);
        List<BusinessCatalogListResponse> dtos = pageResult.stream().map((c) -> {
            BusinessCatalogListResponse<Long> dto = new BusinessCatalogListResponse<>();
            dto.setId(c.getSecureId());
            dto.setIndex(c.getId());
            dto.setTitle(c.getTitle());
            dto.setDescription(c.getDescription());
            dto.setImage(GlobalConverter.getParseImage(c.getImage(), baseUrl));
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    public BusinessCatalogDetailResponse getDetailBusinessCatalog(String catalogId) {

        BusinessCatalog businessCatalog = HandlerRepository.getEntityBySecureId(catalogId, businessCatalogRepository, "catalog not found");
        BusinessCatalogDetailResponse dto = new BusinessCatalogDetailResponse();
        dto.setId(businessCatalog.getSecureId());
        dto.setTitle(businessCatalog.getTitle());
        dto.setDescription(businessCatalog.getDescription());
        dto.setImage(GlobalConverter.getParseImage(businessCatalog.getImage(), baseUrl));
        return dto;
    }

    public BusinessCatalog saveCatalog(BusinessCatalog businessCatalog) {
        return businessCatalogRepository.save(businessCatalog);
    }

    public BusinessCatalog getCatalogBySecureId(String secureId) {
        return businessCatalogRepository.findBySecureId(secureId).orElse(null);
    }


    public BusinessCatalog getCatalogById(Long id) {
        Optional<BusinessCatalog> catalog = businessCatalogRepository.findById(id);
        return catalog.orElse(null);
    }

    public List<BusinessCatalog> getAllCatalogs() {
        return businessCatalogRepository.findAll();
    }

    public BusinessCatalog updateCatalog(String secureId, BusinessCatalog catalogDetails) {
        Optional<BusinessCatalog> catalogOptional = businessCatalogRepository.findBySecureId(secureId);

        if (catalogOptional.isPresent()) {
            BusinessCatalog catalog = catalogOptional.get();
            catalog.setTitle(catalogDetails.getTitle());
            catalog.setImage(catalogDetails.getImage());
            catalog.setDescription(catalogDetails.getDescription());
            return businessCatalogRepository.save(catalog);
        } else {
            return null;
        }
    }

    public boolean deleteCatalogBySecureId(String secureId) {
        Optional<BusinessCatalog> catalog = businessCatalogRepository.findBySecureId(secureId);
        if (catalog.isPresent()) {
            businessCatalogRepository.delete(catalog.get());
            return true;
        }
        return false;
    }

    public Page<BusinessCatalog> getCatalogsByBusiness(Business business, Pageable pageable) {
        return businessCatalogRepository.findByBusiness(business, pageable);
    }

    public List<BusinessCatalogCountsResponse> getTotalCatalogs() {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);

        return businessRepository.getBusinessCatalogsCount(user.getId());
    }
}

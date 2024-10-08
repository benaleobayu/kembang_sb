package com.bca.byc.service;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeOnboarding;
import com.bca.byc.converter.parsing.TreeOnboardingUpdate;
import com.bca.byc.converter.parsing.TreeUserResponse;
import com.bca.byc.entity.*;
import com.bca.byc.exception.ForbiddenException;
import com.bca.byc.model.BusinessDetailResponse;
import com.bca.byc.model.BusinessTreeRequest;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.repository.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessHasCategoryRepository businessHasCategoryRepository;
    private final BusinessHasLocationRepository businessHasLocationRepository;
    private final BusinessCatalogRepository businessCatalogRepository;
    private final LocationRepository locationRepository;
    private final BusinessCategoryRepository businessCategoryRepository;

    private final AppUserRepository appUserRepository;

    // Method to get a Business by its secureId
    public BusinessDetailResponse getBusinessBySecureId(String secureId) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);
        Business business = HandlerRepository.getEntityBySecureId(secureId, businessRepository, "Business not found");

        if (business.getUser().getId() != user.getId()) {
            throw new ForbiddenException("You are not authorized to access this resource");
        }
        BusinessDetailResponse dto = new BusinessDetailResponse();
        TreeUserResponse.convertSingleBusinesses(business, dto);
        return dto;
    }

    // Method to find Business by secureId and userId
    public Business getBusinessBySecureIdAndUserId(String secureId, Long userId) {
        Optional<Business> business = businessRepository.findBySecureIdAndUser_Id(secureId, userId);
        return business.orElse(null); // Return null if no matching Business is found
    }


    // Method to find Business by secureId and userId
    public Page<Business> getBussinessByUserIdPage(Long userId, Pageable pageable) {
        return businessRepository.findByUser_Id(userId, pageable);
    }


    // Method to save BusinessHasCategory
    public BusinessHasCategory saveBusinessHasCategory(BusinessHasCategory businessHasCategory) {
        return businessHasCategoryRepository.save(businessHasCategory); // Save and return the entity
    }

    // Method to save BusinessHasLocation
    public BusinessHasLocation saveBusinessHasLocation(BusinessHasLocation businessHasLocation) {
        return businessHasLocationRepository.save(businessHasLocation); // Save and return the entity
    }

    // Method to save Business
    public Business saveBusiness(Business business) {
        return businessRepository.save(business); // Save and return the Business entity
    }

    // Method to get Location by ID
    public Optional<Location> getLocationById(Long locationId) {
        return locationRepository.findById(locationId); // Fetch location by ID
    }

    // Method to delete associated BusinessHasCategory entries by Business
    @Transactional
    public void deleteBusinessHasCategoriesByBusiness(Business business) {
        // Delete all BusinessHasCategory entries associated with the business
        businessHasCategoryRepository.deleteByBusiness(business);
    }

    // Method to delete associated BusinessHasLocation entries by Business
    @Transactional
    public void deleteBusinessHasLocationsByBusiness(Business business) {
        // Delete all BusinessHasLocation entries associated with the business
        businessHasLocationRepository.deleteByBusiness(business);
    }

    @Transactional
    public void deleteBusinessCatalogsByBusiness(Business business) {
        // Delete all BusinessCatalog entries associated with the business
        businessCatalogRepository.deleteByBusiness(business);
    }


    // Method to delete the business
    @Transactional
    public void deleteBusiness(String businessId) {
        Business business = HandlerRepository.getEntityBySecureId(businessId, businessRepository, "Business not found");
        // Delete the business entity
        businessRepository.delete(business);
    }

    public ResultPageResponseDTO<BusinessListResponse> listDataBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Business> pageResult = businessRepository.findBusinessByKeyword(user.getId(), keyword, pageable);
        List<BusinessListResponse> dtos = TreeUserResponse.convertListBusinesses(pageResult.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Transactional
    public void createNewBusiness(BusinessTreeRequest dto) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);

        TreeOnboarding converter = new TreeOnboarding();
        Business newBusiness = converter.createBusiness(dto, user, false, businessRepository);
        Business savedBusiness = businessRepository.save(newBusiness);

        converter.handleBusinessCategories(dto.getCategoryItemIds(), savedBusiness, businessCategoryRepository, businessHasCategoryRepository);
        converter.handleBusinessLocations(dto.getLocationIds(), savedBusiness, locationRepository, businessHasLocationRepository);

    }

    @Transactional
    public void updateBusiness(String secureId, BusinessTreeRequest dto) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);

        TreeOnboardingUpdate converter = new TreeOnboardingUpdate();
        Business dataBusiness = HandlerRepository.getEntityBySecureId(secureId, businessRepository, "Business not found");

        Business updatedBusiness = converter.updateBusiness(dataBusiness, dto, user, false, businessRepository);

        converter.handleUpdateBusinessCategories(dto.getCategoryItemIds(), updatedBusiness, businessCategoryRepository, businessHasCategoryRepository);
        converter.handleUpdateBusinessLocations(dto.getLocationIds(), updatedBusiness, locationRepository, businessHasLocationRepository);
    }

}

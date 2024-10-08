package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessTreeRequest;
import com.bca.byc.model.ExpectCategoryTreeRequest;
import com.bca.byc.repository.*;
import com.bca.byc.repository.handler.HandlerRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class TreeOnboardingUpdate {

    public void handleUpdateBusinessCategories(List<String> categoryItemIds,
                                               Business business,
                                               BusinessCategoryRepository businessCategoryRepository,
                                               BusinessHasCategoryRepository businessHasCategoryRepository) throws BadRequestException {
        businessHasCategoryRepository.deleteByBusiness(business);

        for (String childCategoryId : categoryItemIds) {
            BusinessCategory childCategory = HandlerRepository.getIdBySecureId(
                    childCategoryId,
                    businessCategoryRepository::findByIdAndSecureId,
                    projection -> businessCategoryRepository.findById(projection.getId()),
                    "Business Category not found for Child ID: " + childCategoryId
            );

            BusinessCategory parentCategory = childCategory.getParentId();
            if (parentCategory == null) {
                throw new BadRequestException("Parent Category not found for Child ID: " + childCategoryId);
            }

            BusinessHasCategory businessHasCategory = new BusinessHasCategory();
            businessHasCategory.setBusiness(business);
            businessHasCategory.setBusinessCategoryParent(parentCategory);
            businessHasCategory.setBusinessCategoryChild(childCategory);
            businessHasCategory.setCreatedAt(LocalDateTime.now());
            businessHasCategory.setUpdatedAt(LocalDateTime.now());

            businessHasCategoryRepository.save(businessHasCategory);
        }
    }

    public void handleUpdateBusinessLocations(List<String> locationIds,
                                              Business business,
                                              LocationRepository locationRepository,
                                              BusinessHasLocationRepository businessHasLocationRepository) throws BadRequestException {
        businessHasLocationRepository.deleteByBusiness(business);

        Set<Location> locations = new HashSet<>();
        for (String locationId : locationIds) {
            Location location = HandlerRepository.getIdBySecureId(
                    locationId,
                    locationRepository::findByIdAndSecureId,
                    projection -> locationRepository.findById(projection.getId()),
                    "Location not found for ID: " + locationId
            );
            locations.add(location);
        }

        for (Location location : locations) {
            BusinessHasLocation businessHasLocation = new BusinessHasLocation();
            businessHasLocation.setBusiness(business);
            businessHasLocation.setLocation(location);
            businessHasLocationRepository.save(businessHasLocation);
        }
    }


    public Business updateBusiness(Business existingBusiness, BusinessTreeRequest dto, AppUser user, boolean isFirstBusiness, BusinessRepository businessRepository) {
        existingBusiness.setName(dto.getBusinessName());
        existingBusiness.setAddress(dto.getBusinessAddress());
        existingBusiness.setUser(user);
        boolean newIsPrimary = isFirstBusiness || (dto.getIsPrimary() != null && dto.getIsPrimary());

        if (newIsPrimary) {
            List<Business> existingBusinesses = businessRepository.findByUser(user);
            for (Business business : existingBusinesses) {
                if (business.getId() != null && business.getIsPrimary() && !business.getId().equals(existingBusiness.getId())) {
                    business.setIsPrimary(false);
                    businessRepository.save(business);
                }
            }
        }

        existingBusiness.setIsPrimary(newIsPrimary); // Update primary status
        return businessRepository.save(existingBusiness); // Save updates
    }

    public void handleExpectCategories(List<ExpectCategoryTreeRequest> expectCategories,
                                       AppUser user,
                                       ExpectCategoryRepository expectCategoryRepository,
                                       ExpectItemRepository expectItemRepository,
                                       UserHasExpectRepository userHasExpectRepository
    ) throws BadRequestException {
        ExpectCategory specialExpectCategory = expectCategoryRepository.findById(5L)
                .orElseThrow(() -> new BadRequestException("Expect Category not found"));

        String expectCategoryIdForSpecialCase = specialExpectCategory.getSecureId();

        for (ExpectCategoryTreeRequest expectDto : expectCategories) {
            ExpectCategory expectCategory = HandlerRepository.getIdBySecureId(
                    expectDto.getExpectCategoryId(),
                    expectCategoryRepository::findBySecureId,
                    projection -> expectCategoryRepository.findById(projection.getId()),
                    "Expect Category not found"
            );

            if (expectDto.getItems() != null && expectDto.getItems().getIds() != null && !expectDto.getItems().getIds().isEmpty()) {
                handleExpectItems(expectDto, user, expectCategory, expectItemRepository, userHasExpectRepository);
            } else if (expectDto.getExpectCategoryId().equals(expectCategoryIdForSpecialCase)) {
                handleSpecialExpect(user, expectCategory, expectItemRepository, userHasExpectRepository);
            }
        }
    }

    public void handleExpectItems(ExpectCategoryTreeRequest expectDto,
                                  AppUser user,
                                  ExpectCategory expectCategory,
                                  ExpectItemRepository expectItemRepository,
                                  UserHasExpectRepository userHasExpectRepository
    ) throws BadRequestException {
        for (String expectItemId : expectDto.getItems().getIds()) {
            ExpectItem expectItem = HandlerRepository.getIdBySecureId(
                    expectItemId,
                    expectItemRepository::findBySecureId,
                    projection -> expectItemRepository.findById(projection.getId()),
                    "Expect Item not found"
            );

            UserHasExpectId userHasExpectId = new UserHasExpectId();
            userHasExpectId.setUserId(user.getId());
            userHasExpectId.setExpectCategoryId(expectCategory.getId());
            userHasExpectId.setExpectItemId(expectItem.getId());

            UserHasExpect userHasExpect = new UserHasExpect();
            userHasExpect.setId(userHasExpectId);
            userHasExpect.setUser(user);
            userHasExpect.setExpectCategory(expectCategory);
            userHasExpect.setExpectItem(expectItem);
            userHasExpect.setOtherExpect(expectDto.getOtherExpect());
            userHasExpect.setOtherExpectItem(expectDto.getItems().getOtherExpectItem());

            userHasExpectRepository.save(userHasExpect);
        }
    }

    public void handleSpecialExpect(AppUser user,
                                    ExpectCategory expectCategory,
                                    ExpectItemRepository expectItemRepository,
                                    UserHasExpectRepository userHasExpectRepository
    ) throws BadRequestException {
        UserHasExpect userHasExpect = new UserHasExpect();
        ExpectItem expectItem = expectItemRepository.findById(5L)
                .orElseThrow(() -> new BadRequestException("Expect Item not found"));
        userHasExpect.setUser(user);
        userHasExpect.setExpectCategory(expectCategory);
        userHasExpect.setExpectItem(expectItem);
        userHasExpectRepository.save(userHasExpect);
    }

}

package com.bca.byc.service.impl;

import com.bca.byc.controller.api.LocationResource;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.OnboardingModelDTO;
import com.bca.byc.repository.*;
import com.bca.byc.service.OnboardingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class OnboardingServiceImpl implements OnboardingService {

    private UserRepository userRepository;
    private BusinessRepository businessRepository;
    private BusinessCategoryRepository businessCategoryRepository;
    private BusinessHasCategoryRepository businessHasCategoryRepository;
    private ExpectCategoryRepository expectCategoryRepository;
    private ExpectItemRepository expectItemRepository;
    private UserHasExpectRepository userHasExpectRepository;
    private LocationRepository locationRepository;


    @Override
    public void createData(String email, @Valid OnboardingModelDTO.OnboardingCreateRequest dto) throws BadRequestException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Create Businesses
        for (OnboardingModelDTO.OnboardingBusinessRequest businessDto : dto.getBusinesses()) {
            Business business = new Business();
            business.setName(businessDto.getBusinessName());
            business.setAddress(businessDto.getBusinessAddress());
            business.setStatus(true);
            business.setOrders(0);
            business.setUser(user);
            business = businessRepository.save(business);

            // Create Business Categories
            for (OnboardingModelDTO.OnboardingBusinessCategoryRequest categoryDto : businessDto.getBusinessCategories()) {
                BusinessCategory parentCategory = businessCategoryRepository.findById(categoryDto.getBusinessCategoryId())
                        .orElseThrow(() -> new RuntimeException("Business Category Parent not found"));

                BusinessCategory childCategory = businessCategoryRepository.findById(categoryDto.getBusinessCategoryChildId())
                        .orElseThrow(() -> new RuntimeException("Business Category Child not found"));

                BusinessHasCategory businessHasCategory = new BusinessHasCategory();
                businessHasCategory.setBusiness(business);
                businessHasCategory.setBusinessCategoryParent(parentCategory);
                businessHasCategory.setBusinessCategoryChild(childCategory);
                businessHasCategory.setCreatedAt(LocalDateTime.now());
                businessHasCategory.setUpdatedAt(LocalDateTime.now());

                // Save the BusinessHasCategory entity
                businessHasCategoryRepository.save(businessHasCategory);
            }

            // Handle Business Locations
            Set<Location> locations = new HashSet<>();
            for (OnboardingModelDTO.OnboardingLocationRequest locationDto : businessDto.getLocations()) {
                Location location = locationRepository.findById(locationDto.getLocationId())
                        .orElseThrow(() -> new BadRequestException("Location not found"));
                locations.add(location);
            }
            business.setLocations(locations);
        }
        // Create Expect
        for (OnboardingModelDTO.OnboardingExpectCategoryResponse expectDto : dto.getExpectCategories()) {
            ExpectCategory expectCategory = expectCategoryRepository.findById(expectDto.getExpectCategoryId())
                    .orElseThrow(() -> new BadRequestException("Expect Category not found"));

            // Fetch the associated ExpectItem, if required
            ExpectItem expectItem = expectItemRepository.findById(expectDto.getExpectCategoryItemId())
                    .orElseThrow(() -> new BadRequestException("Expect Item not found"));

            // Create and set the UserHasExpectId
            UserHasExpectId userHasExpectId = new UserHasExpectId();
            userHasExpectId.setUserId(user.getId());
            userHasExpectId.setExpectCategoryId(expectCategory.getId());
            userHasExpectId.setExpectItemId(expectItem.getId());

            // Create UserHasExpect and associate it with User, ExpectCategory, and ExpectItem
            UserHasExpect userHasExpect = new UserHasExpect();
            userHasExpect.setId(userHasExpectId);
            userHasExpect.setUser(user);
            userHasExpect.setExpectCategory(expectCategory);
            userHasExpect.setExpectItem(expectItem);

            // Save UserHasExpect
            userHasExpectRepository.save(userHasExpect);
        }

        user.setStatus(StatusType.ACTIVATED);
        userRepository.save(user);

    }
}

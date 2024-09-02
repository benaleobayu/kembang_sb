package com.bca.byc.service.impl;

import com.bca.byc.convert.OnboardingDTOConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.model.OnboardingModelDTO;
import com.bca.byc.repository.*;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.OnboardingService;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private OnboardingDTOConverter onboardingConverter;


    @Override
    @Transactional
    public void createData(String email, @Valid OnboardingModelDTO.OnboardingCreateRequest dto) throws BadRequestException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("User not found"));

            // Create Businesses
            for (OnboardingModelDTO.OnboardingBusinessRequest businessDto : dto.getBusinesses()) {
                Business business = new Business();
                business.setName(businessDto.getBusinessName());
                business.setAddress(businessDto.getBusinessAddress());
                business.setUser(user);
                business = businessRepository.save(business);

                // Handle Business Categories via categoryItemIds
                for (Long childCategoryId : businessDto.getCategoryItemIds()) {
                    BusinessCategory childCategory = businessCategoryRepository.findById(childCategoryId)
                            .orElseThrow(() -> new BadRequestException("Business Category Child not found"));

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

                    // Save the BusinessHasCategory entity
                    businessHasCategoryRepository.save(businessHasCategory);
                }

                // Handle Business Locations via locationIds
                Set<Location> locations = new HashSet<>();
                for (Long locationId : businessDto.getLocationIds()) {
                    Location location = locationRepository.findById(locationId)
                            .orElseThrow(() -> new BadRequestException("Location not found"));
                    locations.add(location);
                }
                business.setLocations(locations);
            }

            // Create Expect Categories
            for (OnboardingModelDTO.OnboardingExpectCategoryResponse expectDto : dto.getExpectCategories()) {
                ExpectCategory expectCategory = expectCategoryRepository.findById(expectDto.getExpectCategoryId())
                        .orElseThrow(() -> new BadRequestException("Expect Category not found"));

                if (expectDto.getItems() != null && expectDto.getItems().getIds() != null && !expectDto.getItems().getIds().isEmpty()) {
                    for (Long expectItemId : expectDto.getItems().getIds()) {
                        ExpectItem expectItem = expectItemRepository.findById(expectItemId)
                                .orElseThrow(() -> new BadRequestException("Expect Item not found"));

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
                } else if (expectDto.getExpectCategoryId() == 5) {
                    UserHasExpect userHasExpect = new UserHasExpect();
                    ExpectItem expectItem = expectItemRepository.findById(5L)
                            .orElseThrow(() -> new BadRequestException("Expect Item not found"));
                    userHasExpect.setUser(user);
                    userHasExpect.setExpectCategory(expectCategory);
                    userHasExpect.setExpectItem(expectItem);
                    userHasExpect.setOtherExpect(expectDto.getOtherExpect());
                    userHasExpectRepository.save(userHasExpect);
                }
            }

            user.setStatus(StatusType.ACTIVATED);
            userRepository.save(user);

        } catch (Exception e) {
            // Log the exception for debugging
            LoggerFactory.getLogger(getClass()).error("Onboarding creation failed: ", e);
            throw new BadRequestException("Onboarding creation failed: " + e.getMessage());
        }
    }

    @Override
    public ResultPageResponseDTO<OnboardingListUserResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<User> pageResult = userRepository.findByNameLikeIgnoreCaseAndStatusAndUserAttributesIsRecommendedTrue(userName, StatusType.ACTIVATED,pageable);
        List<OnboardingListUserResponse> dtos = pageResult.stream().map((c) -> {
            OnboardingListUserResponse dto = onboardingConverter.convertToListOnboardingResponse(c);
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
}

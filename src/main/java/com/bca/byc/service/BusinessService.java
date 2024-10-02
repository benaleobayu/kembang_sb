package com.bca.byc.service;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessHasCategory;
import com.bca.byc.entity.BusinessHasLocation;
import com.bca.byc.entity.Location;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.BusinessCatalogRepository;
import com.bca.byc.repository.BusinessHasCategoryRepository;
import com.bca.byc.repository.BusinessHasLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessHasCategoryRepository businessHasCategoryRepository;

    @Autowired
    private BusinessHasLocationRepository businessHasLocationRepository;

    @Autowired
    private BusinessCatalogRepository businessCatalogRepository;
    @Autowired
    private LocationRepository locationRepository;

    // Method to get a Business by its secureId
    public Business getBusinessBySecureId(String secureId) {
        Optional<Business> business = businessRepository.findBySecureId(secureId);
        return business.orElse(null); // Return the Business if present, otherwise return null
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
     public void deleteBusiness(Business business) {
         // Delete the business entity
         businessRepository.delete(business);
     }

}

package com.bca.byc.service;

import com.bca.byc.entity.Business;
import com.bca.byc.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

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

    
}

package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    boolean existsByBusinessCategories(BusinessCategory data);

    Optional<Business> findBySecureId(String secureId);
    // Method to find a Business by secure_id and userId
    Optional<Business> findBySecureIdAndUser_Id(String secureId, Long userId);
    // Optional<Business> findBySecureIdAndUserId(String secureId, Long userId);
}

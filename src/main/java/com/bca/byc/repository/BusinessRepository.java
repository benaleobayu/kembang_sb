package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    // Check if a business exists by its categories
    boolean existsByBusinessCategories(BusinessCategory category);

    // Find a Business by secure ID
    Optional<Business> findBySecureId(String secureId);

    // Method to find a Business by secure_id and user ID
    Optional<Business> findBySecureIdAndUser_Id(String secureId, Long userId);

    // // Find a Business by user ID
    // Optional<Business> findByUserId(Long userId);

    // Pagination method to find businesses by user ID
    Page<Business> findByUser_Id(Long userId, Pageable pageable);
}

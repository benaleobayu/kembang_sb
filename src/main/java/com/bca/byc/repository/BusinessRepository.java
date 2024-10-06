package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.BusinessCatalogCountsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("SELECT b FROM Business b " +
            "JOIN BusinessHasCategory bhc ON bhc.business.id = b.id " +
            "JOIN BusinessCategory bc ON bc.id = bhc.business.id " +
            "WHERE (LOWER(bc.name) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "b.user.id = :id")
    Page<Business> findBusinessByKeyword(@Param("id") Long id, @Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT new com.bca.byc.model.BusinessCatalogCountsResponse(" +
            "b.secureId, b.id, b.name, COUNT(bc)) " +
            "FROM Business b " +
            "JOIN BusinessCatalog bc ON bc.business.id = b.id " +
            "WHERE b.user.id = :id " +
            "GROUP BY b")
    List<BusinessCatalogCountsResponse> getBusinessCatalogsCount(Long id);
}

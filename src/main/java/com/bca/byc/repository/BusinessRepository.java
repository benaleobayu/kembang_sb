package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.model.BusinessCatalogCountsResponse;
import com.bca.byc.model.export.BusinessExportResponse;
import com.bca.byc.model.projection.BusinessExportProjection;
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

    // Find a Business by secure ID
    Optional<Business> findBySecureId(String secureId);

    // Method to find a Business by secure_id and user ID
    Optional<Business> findBySecureIdAndUser_Id(String secureId, Long userId);

    @Query("""
            SELECT b FROM Business b WHERE b.user.id = :userId
            """)
    List<Business> findBusinessByUserId(@Param("userId") Long userId);

    // // Find a Business by user ID
    // Optional<Business> findByUserId(Long userId);

    // Pagination method to find businesses by user ID
    Page<Business> findByUser_Id(Long userId, Pageable pageable);

    @Query("SELECT b FROM Business b " +
            "WHERE (LOWER(b.name) LIKE LOWER(:keyword)) AND " +
            "b.isActive = true AND " +
            "b.user.id = :id " +
            "ORDER BY b.isPrimary DESC, b.id ASC")
    Page<Business> findBusinessByKeyword(@Param("id") Long id, @Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT new com.bca.byc.model.BusinessCatalogCountsResponse(" +
            "b.secureId, b.id, b.name, COUNT(bc)) " +
            "FROM Business b " +
            "JOIN BusinessCatalog bc ON bc.business.id = b.id " +
            "WHERE b.user.id = :id " +
            "GROUP BY b")
    List<BusinessCatalogCountsResponse> getBusinessCatalogsCount(Long id);

    List<Business> findByUser(AppUser user);

    // --- for detail usermanagement ---
    @Query("SELECT b FROM Business b " +
            "WHERE " +
            "(LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "(:id IS NULL OR b.user.id IN :id)")
    Page<Business> findBusinessOnUser(@Param("id") List<Long> id, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b.id as id, b.name as name, b.address as address, bc.name as lineOfBusiness, b.isPrimary as isPrimary, " +
            "u.email as userEmail, u.appUserDetail.name as userName, u.appUserDetail.memberCin as userCin " +
            "FROM Business b " +
            "LEFT JOIN BusinessHasCategory bhc ON bhc.business.id = b.id " +
            "LEFT JOIN BusinessCategory bc ON bc.id = bhc.businessCategoryParent.id " +
            "LEFT JOIN AppUser u ON u.id = b.user.id")
    List<BusinessExportProjection> findDataForExportByUserId();
    // --- for detail usermanagement ---
}
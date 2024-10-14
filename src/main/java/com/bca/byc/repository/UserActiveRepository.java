package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.model.projection.CmsGetIdFromSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserActiveRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u " +
            "LEFT JOIN u.appUserDetail aud " +
            "LEFT JOIN aud.branchCode bc " +
            "LEFT JOIN u.businesses b " +
            "LEFT JOIN b.businessHasLocations bhl " +
            "LEFT JOIN bhl.location loc " +
            "WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.memberBankAccount) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) AND " +
            "(:locationId IS NULL OR loc.id = :locationId) AND " +
            "aud.status = 6 AND " +
            "u.appUserAttribute.isSuspended IN (false) AND " +
            "u.appUserAttribute.isDeleted IN (false) AND " +
            "u.appUserDetail.createdAt BETWEEN :startDate AND :endDate")
    Page<AppUser> findByKeywordAndStatusAndCreatedAt(@Param("keyword") String keyword,
                                                     @Param("locationId") Long locationId,
                                                     @Param("startDate") LocalDateTime start,
                                                     @Param("endDate") LocalDateTime end,
                                                     Pageable pageable);

    @Query("SELECT u FROM AppUser u ")
    List<UserActiveExportResponse> findAllData();

    @Query("SELECT new com.bca.byc.model.export.UserActiveExportResponse(" +
            "branch.name, aud.name, " +
            "CASE WHEN aud.userAs is null OR aud.userAs = 'member' THEN aud.memberBirthdate ELSE aud.parentBirthdate END, " +
            "u.email, " +
            "CASE WHEN aud.userAs is null OR aud.userAs = 'member' THEN aud.memberCin ELSE aud.parentCin END, " +
            "aud.phone, u.createdAt) " +
            "FROM AppUser u " +
            "LEFT JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "LEFT JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "LEFT JOIN aud.branchCode branch " +
            "WHERE " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false " +
            "ORDER BY u.createdAt DESC")
    List<UserActiveExportResponse> findDataForExport();

    @Query("SELECT new com.bca.byc.model.data.ListTagUserResponse(" +
            "u.secureId, u.id, aud.avatar, aud.name, " +
            "bhc.business.name, bhc.businessCategoryParent.name, b.isPrimary " + // Include business fields
            ") " +
            "FROM AppUser u " +
            "JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "LEFT JOIN Business b ON b.user.id = u.id AND b.isPrimary = true " + // Filter for primary businesses
            "LEFT JOIN BusinessHasCategory bhc ON bhc.business.id = b.id " +
            "LEFT JOIN BusinessCategory bc ON bc.id = bhc.businessCategoryParent.id " +
            "WHERE " +
            "(LOWER(aud.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) AND " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false " +
            "GROUP BY u.id, aud.avatar, aud.name, bhc.business.name, bhc.businessCategoryParent.name, b.isPrimary ")
    Page<ListTagUserResponse> findListTagUser(String keyword, Pageable pageable);

    Optional<AppUser> findBySecureId(String id);

    boolean existsBySecureId(String id);

    @Query("SELECT u " +
            "FROM AppUser u " +
            "LEFT JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "WHERE u.secureId IN :ids")
    Set<CmsGetIdFromSecureIdProjection> findToSuspendBySecureIdIn(@Param("ids") Set<String> ids);
}


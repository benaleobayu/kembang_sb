package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.model.export.UserActiveExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActiveRepository extends JpaRepository<AppUser, Long> {


    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsSuspendedFalse(String userName, StatusType statusType, Pageable pageable);

    @Query("SELECT u FROM AppUser u " +
            "JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "LEFT JOIN Business b ON b.user.id = u.id " +
            "LEFT JOIN BusinessHasLocation bhl ON bhl.business.id = b.id " +
            "LEFT JOIN Location loc ON loc.id = bhl.location.id " +
            "WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.memberBankAccount) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) AND " +
            "(:locationId IS NULL OR loc.id = :locationId) AND " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false AND " +
            "u.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY u.id DESC")
    Page<AppUser> findByKeywordAndStatusAndCreatedAt(@Param("keyword") String keyword,
                                                     @Param("locationId") Long locationId,
                                                     @Param("startDate") LocalDateTime start,
                                                     @Param("endDate") LocalDateTime end,
                                                     Pageable pageable);

    @Query("SELECT u FROM AppUser u ")
    List<UserActiveExportResponse> findAllData();

    @Query("SELECT new com.bca.byc.model.export.UserActiveExportResponse(" +
            "u.id, loc.name, aud.name, " +
            "CASE WHEN aud.userAs is null OR aud.userAs = 'member' THEN aud.memberBirthdate ELSE aud.parentBirthdate END, " +
            "u.email, " +
            "CASE WHEN aud.userAs is null OR aud.userAs = 'member' THEN aud.memberCin ELSE aud.parentCin END, " +
            "aud.phone, u.createdAt) " +
            "FROM AppUser u " +
            "LEFT JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "LEFT JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "LEFT JOIN Location  loc ON loc.id = u.location.id " +
            "WHERE " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false ")
    List<UserActiveExportResponse> findDataForExport();


    @Query("SELECT new com.bca.byc.model.data.ListTagUserResponse(" +
            "u.id, aud.avatar, aud.name, " +
            "bhc.business.name, bhc.businessCategoryParent.name, b.isPrimary " + // Include business fields
            ") " +
            "FROM AppUser u " +
            "JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "JOIN Business b ON b.user.id = u.id AND b.isPrimary = true " + // Filter for primary businesses
            "LEFT JOIN BusinessHasCategory bhc ON bhc.business.id = b.id " +
            "LEFT JOIN BusinessCategory bc ON bc.id = bhc.businessCategoryParent.id " +
            "WHERE " +
            "(LOWER(aud.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) AND " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false " +
            "GROUP BY u.id, aud.avatar, aud.name, bhc.business.name, bhc.businessCategoryParent.name, b.isPrimary ")
    Page<ListTagUserResponse> findListTagUser(String keyword, Pageable pageable);

}


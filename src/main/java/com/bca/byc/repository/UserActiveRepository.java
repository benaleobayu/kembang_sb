package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.UserType;
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
            "LEFT JOIN u.businesses b " +
            "LEFT JOIN b.businessHasLocations bhl " +
            "LEFT JOIN bhl.location loc " +
            "WHERE " +
            "(LOWER(u.email) LIKE LOWER(:keyword ) OR " +
            "LOWER(u.appUserDetail.name) LIKE LOWER(:keyword ) OR " +
            "LOWER(u.appUserDetail.phone) LIKE LOWER(:keyword ) OR " +
            "LOWER(u.appUserDetail.memberBankAccount) LIKE LOWER(:keyword ) ) AND " +
            "(:locationId IS NULL OR loc.id = :locationId) AND " +
            "(:isSenior IS NULL OR u.appUserDetail.isSenior = :isSenior) AND " +
            "(:segmentation IS NULL OR u.appUserDetail.memberType = :segmentation) AND " +
            "aud.status = 6 AND " +
            "u.appUserAttribute.isSuspended IN (false) AND " +
            "u.appUserAttribute.isDeleted IN (false) AND " +
            "u.appUserAttribute.isHardDeleted IN (false) AND " +
            "u.appUserDetail.createdAt BETWEEN :startDate AND :endDate")
    Page<AppUser> GetDataIndexUserActive(@Param("keyword") String keyword,
                                         Pageable pageable,
                                         @Param("startDate") LocalDateTime start,
                                         @Param("endDate") LocalDateTime end,
                                         @Param("locationId") Long locationId,
                                         @Param("segmentation") UserType segmentation,
                                         @Param("isSenior") Boolean isSenior);

    @Query("SELECT u FROM AppUser u ")
    List<UserActiveExportResponse> findAllData();

    @Query("SELECT new com.bca.byc.model.export.UserActiveExportResponse(" +
            "branch.name, aud.name, " +
            "aud.memberBirthdate, " +
            "u.email, " +
            "aud.memberCin, " +
            "aud.memberType, " +
            "aud.phone, u.createdAt) " +
            "FROM AppUser u " +
            "LEFT JOIN AppUserDetail aud ON aud.id = u.appUserDetail.id " +
            "LEFT JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "LEFT JOIN aud.branchCode branch " +
            "LEFT JOIN u.businesses b " +
            "LEFT JOIN b.businessHasLocations bhl " +
            "LEFT JOIN bhl.location loc " +
            "WHERE " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false AND " +
            "aua.isHardDeleted = false AND " +
            "(:segmentation IS NULL OR aud.memberType = :segmentation) AND " +
            "(:locationId IS NULL OR loc.id = :locationId) AND " +
            "(:isSenior IS NULL OR aud.isSenior = :isSenior) AND " +
            "u.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY u.createdAt DESC")
    List<UserActiveExportResponse> findDataForExport(@Param("startDate") LocalDateTime start,
                                                     @Param("endDate") LocalDateTime end,
                                                     @Param("segmentation") UserType segmentation,
                                                     @Param("locationId") Long locationId,
                                                     @Param("isSenior") Boolean isSenior);

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
            "(LOWER(aud.name) LIKE LOWER(:keyword ) ) AND " +
            "aud.status = 6 AND " +
            "aua.isSuspended = false AND " +
            "aua.isDeleted = false AND " +
            "aua.isHardDeleted = false " +
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


package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDeletedRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findBySecureId(String userId);

    Set<AppUser> findByIdIn(Set<Long> ids);

    @Query("SELECT u " +
            "FROM AppUser u " +
            "LEFT JOIN AppUserAttribute aua ON aua.id = u.appUserAttribute.id " +
            "WHERE u.secureId IN :ids")
    Set<CMSBulkDeleteProjection> findBySecureIdIn(@Param("ids") Set<String> ids);

    @Query("SELECT u FROM AppUser u " +
            "LEFT JOIN u.appUserDetail aud " +
            "LEFT JOIN aud.branchCode bc " +
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
            "u.appUserAttribute.isSuspended IN (true) AND " +
            "u.appUserAttribute.isDeleted IN (true) AND " +
            "u.appUserAttribute.isHardDeleted IN (false) AND " +
            "u.appUserDetail.createdAt BETWEEN :startDate AND :endDate")
    Page<AppUser> GetDataDeletedUser(@Param("keyword") String keyword,
                                       Pageable pageable,
                                       @Param("startDate") LocalDateTime start,
                                       @Param("endDate") LocalDateTime end,
                                       @Param("locationId") Long locationId,
                                       @Param("segmentation") UserType segmentation,
                                       @Param("isSenior") Boolean isSenior);

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
            "aua.isSuspended = true AND " +
            "aua.isDeleted = true AND " +
            "aua.isHardDeleted = false ")
    List<UserActiveExportResponse> findDataForExport();

}

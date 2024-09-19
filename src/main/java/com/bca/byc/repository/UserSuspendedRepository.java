package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserSuspendedRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsSuspendedTrueAndAppUserAttributeIsDeletedFalse(String userName, StatusType statusType, Pageable pageable);

    Set<AppUser> findByIdIn(Set<Long> ids);

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
            "aua.isSuspended = true AND " +
            "aua.isDeleted = false AND " +
            "u.createdAt BETWEEN :startDate AND :endDate")
    Page<AppUser> findByKeywordAndStatusAndSuspendedAndCreatedAt(@Param("keyword") String keyword,
                                                                 @Param("locationId") Long locationId,
                                                                 @Param("startDate") LocalDateTime start,
                                                                 @Param("endDate") LocalDateTime end,
                                                                 Pageable pageable);
}

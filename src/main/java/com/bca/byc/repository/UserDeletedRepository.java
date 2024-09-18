package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserDeletedRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u " +
            "LEFT JOIN Business b ON b.user.id = u.id " +
            "LEFT JOIN b.locations loc " +
            "WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.memberBankAccount) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) OR " +
            "loc.id = :locationId AND " +
            "u.appUserDetail.status = 6 AND " +
            "u.appUserAttribute.isSuspended = true AND " +
            "u.appUserAttribute.isDeleted = true AND " +
            "u.appUserDetail.createdAt BETWEEN :startDate AND :endDate")
    Page<AppUser> findByKeywordAndStatusAndDeletedAndCreatedAt(@Param("keyword") String keyword,
                                                     @Param("locationId") Long locationId,
                                                     @Param("startDate") LocalDateTime start,
                                                     @Param("endDate") LocalDateTime end,
                                                     Pageable pageable);

    Set<AppUser> findByIdIn(Set<Long> ids);

}

package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserDeletedRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(u.appUserDetail.memberBankAccount) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "u.appUserDetail.status = 6 AND " +
            "u.appUserAttribute.isSuspended = true AND " +
            "u.appUserAttribute.isDeleted = true AND " +
            "u.createdAt BETWEEN :start AND :end")
    Page<AppUser> findByKeywordAndCreatedAtDeletedUser(String keyword, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Set<AppUser> findByIdIn(Set<Long> ids);
}

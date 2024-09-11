package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInquiryRepository extends JpaRepository<AppUser, Long> {
    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatus(String userName, StatusType statusType, Pageable pageable);
}
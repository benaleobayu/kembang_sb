package com.bca.byc.repository.auth;

import java.util.Optional;

import com.bca.byc.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bca.byc.entity.AppUser;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "SELECT au FROM AppUser au " +
            "LEFT JOIN au.appUserDetail aud ON aud.id = au.appUserDetail.id " +
            "LEFT JOIN au.appUserAttribute aua ON aua.id = au.appUserAttribute.id " +
            "WHERE aua.isApproved = true AND aud.status = :statusType AND au.name LIKE :userName")
    Page<AppUser> findOnboardingUser(String userName, StatusType statusType, Pageable pageable);

    Page<AppUser> findByNameLikeIgnoreCase(String name, Pageable pageable);

}

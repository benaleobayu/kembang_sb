package com.bca.byc.repository.auth;

import java.util.Optional;

import com.bca.byc.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bca.byc.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

//    @Query(value = "SELECT au, aud, aua FROM AppUser au " +
//            "LEFT JOIN au.appUserDetail aud ON aud.id = au.appUserDetail.id " +
//            "LEFT JOIN au.appUserAttribute aua ON aua.id = au.appUserAttribute.id " +
//            "WHERE aud.status = 6")
    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsRecommendedTrue(String userName, StatusType statusType, Pageable pageable);

    Page<AppUser> findByNameLikeIgnoreCase(String name, Pageable pageable);

}

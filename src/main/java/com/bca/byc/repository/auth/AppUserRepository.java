package com.bca.byc.repository.auth;

import java.util.Optional;

import com.bca.byc.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bca.byc.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u  FROM AppUser u WHERE u.email = :email")
	Optional<AppUser> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM AppUser u WHERE u.secureId = :userId")
    Optional<AppUser> findBySecureId(@Param("userId") String secureId);

    List<AppUser> findBySecureIdIn(List<String> secureIds); 

//    @Query(value = "SELECT au, aud, aua FROM AppUser au " +
//            "LEFT JOIN au.appUserDetail aud ON aud.id = au.appUserDetail.id " +
//            "LEFT JOIN au.appUserAttribute aua ON aua.id = au.appUserAttribute.id " +
//            "WHERE aud.status = 6")
    Page<AppUser> findByNameLikeIgnoreCaseAndAppUserDetailStatusAndAppUserAttributeIsRecommendedTrue(String userName, StatusType statusType, Pageable pageable);

    Page<AppUser> findByNameLikeIgnoreCase(String name, Pageable pageable);

}


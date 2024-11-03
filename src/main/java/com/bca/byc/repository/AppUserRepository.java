package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.projection.CastIdBySecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u  FROM AppUser u WHERE u.email = :email")
    Optional<AppUser> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM AppUser u WHERE u.secureId = :userId")
    Optional<AppUser> findBySecureId(@Param("userId") String secureId);

    @Query("""
            SELECT new com.bca.byc.model.projection.CastIdBySecureIdProjection(
            u.id, u.secureId)
            FROM AppUser u
            WHERE u.secureId IN (:userId)
            """)
    List<CastIdBySecureIdProjection> findIdBySecureIdList(List<String> userId);

    List<AppUser> findBySecureIdIn(List<String> secureIds);


}


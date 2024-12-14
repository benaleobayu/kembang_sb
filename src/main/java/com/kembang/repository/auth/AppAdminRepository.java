package com.kembang.repository.auth;

import com.kembang.entity.AppAdmin;
import com.kembang.model.projection.IdSecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppAdminRepository extends JpaRepository<AppAdmin, Long> {

    Optional<AppAdmin> findByEmail(String email);

    Optional<AppAdmin> findBySecureId(String secureId);

    @Query("SELECT new com.kembang.model.projection.IdSecureIdProjection(a.id, a.secureId) FROM AppAdmin a WHERE a.secureId = :s")
    Optional<IdSecureIdProjection> findIdBySecureId(String s);

    boolean existsByEmail(String email);

    boolean existsByRoleId(Long roleId);
}

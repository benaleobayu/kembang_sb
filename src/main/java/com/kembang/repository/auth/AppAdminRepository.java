package com.kembang.repository.auth;

import com.kembang.entity.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppAdminRepository extends JpaRepository<AppAdmin, Long> {

    Optional<AppAdmin> findByEmail(String email);

    Optional<AppAdmin> findBySecureId(String secureId);

    boolean existsByEmail(String email);

    boolean existsByRoleId(Long roleId);
}

package com.bca.byc.repository.auth;

import com.bca.byc.entity.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppAdminRepository extends JpaRepository<AppAdmin, Long> {

    Optional<AppAdmin> findByEmail(String email);
}

package com.bca.byc.repository;

import com.bca.byc.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, Long> {
}

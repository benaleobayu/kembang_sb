package com.bca.byc.repository;

import com.bca.byc.entity.RoleHasPermissions;
import com.bca.byc.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleHasPermissionsRepository extends JpaRepository<RoleHasPermissions, RolePermissionId> {
    List<RoleHasPermissions> findByRoleId(Long roleId);
}
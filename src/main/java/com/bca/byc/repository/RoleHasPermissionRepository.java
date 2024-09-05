package com.bca.byc.repository;

import com.bca.byc.entity.Permission;
import com.bca.byc.entity.Role;
import com.bca.byc.entity.RoleHasPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHasPermissionRepository extends JpaRepository<RoleHasPermission, Long> {

    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

    boolean existsByRoleAndPermission(Role data, Permission permission);

    RoleHasPermission findByRoleIdAndPermissionId(Long roleId, Long permissionId);

}

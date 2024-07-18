package com.dev.byc.repository;

import com.dev.byc.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Add custom query methods if needed
}

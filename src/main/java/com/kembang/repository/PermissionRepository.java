package com.kembang.repository;

import com.kembang.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {


    Optional<Permission> findByName(String name);

    boolean existsByName(String name);
}

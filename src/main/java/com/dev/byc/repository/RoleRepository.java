package com.dev.byc.repository;

import com.dev.byc.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Add custom query methods if needed
}

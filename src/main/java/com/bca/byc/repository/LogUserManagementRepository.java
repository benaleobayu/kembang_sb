package com.bca.byc.repository;

import com.bca.byc.entity.UserManagementLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogUserManagementRepository extends JpaRepository<UserManagementLog, Long> {
}

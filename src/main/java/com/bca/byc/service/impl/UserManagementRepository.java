package com.bca.byc.service.impl;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserManagementRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u " +
            "FROM AppUser u " +
            "WHERE u.secureId IN :ids")
    Set<CMSBulkDeleteProjection> findToDeleteBySecureIdIn(Set<String> ids);
}

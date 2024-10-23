package com.bca.byc.repository;

import com.bca.byc.entity.AppAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AppAdmin, Long> {

    Optional<AppAdmin> findByEmail(String email);

    @Query("SELECT a FROM AppAdmin a " +
            "LEFT JOIN a.role r " +
            "WHERE " +
            "(LOWER(a.name) LIKE(:keyword) OR " +
            "LOWER(a.email) LIKE(:keyword)) AND " +
            "(:roleId IS NULL OR r.id = :roleId) AND " +
            "(:status IS NULL OR a.isActive = :status) AND " +
            "a.isDeleted = false AND a.id != 1")
    Page<AppAdmin> getAdminList(@Param("keyword") String keyword, Pageable pageable,@Param("roleId") Long roleId,@Param("status") Boolean status);
}
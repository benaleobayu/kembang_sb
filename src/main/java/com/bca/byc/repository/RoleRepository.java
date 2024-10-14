package com.bca.byc.repository;

import com.bca.byc.entity.Role;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findBySecureId(String id);

    Optional<Role> findByName(String name);

    Page<Role> findByNameLikeIgnoreCase(String userName, Pageable pageable);

    @Query("SELECT r.name FROM Role r")
    List<String> findAllAdminRoles();

    @Query("SELECT r.secureId AS secureId, r.name AS name FROM Role r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND r.isActive = true AND r.isDeleted = false")
    Page<CastSecureIdAndNameProjection> findSecureIdAndName(String keyword, Pageable pageable);
}

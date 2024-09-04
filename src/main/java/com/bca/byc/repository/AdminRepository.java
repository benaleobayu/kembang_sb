package com.bca.byc.repository;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AppAdmin, Long> {

    Optional<AppAdmin> findByEmail(String email);

    Page<AppAdmin> findByNameLikeIgnoreCase(String userName, Pageable pageable);
}
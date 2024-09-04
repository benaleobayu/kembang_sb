package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreRegisterRepository extends JpaRepository<PreRegister, Long> {

    boolean existsByEmail(String email);

    Page<PreRegister> findByNameLikeIgnoreCase(String userName, Pageable pageable);

}

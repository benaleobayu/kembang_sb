package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PreRegisterRepository extends JpaRepository<PreRegister, Long> {

    boolean existsByEmail(String email);

    Page<PreRegister> findByNameLikeIgnoreCaseAndCreatedAtBetween(String userName, LocalDate startDate, LocalDate endDate, Pageable pageable);


    @Query("SELECT p FROM PreRegister p WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.memberBankAccount) LIKE CONCAT('%', :keyword, '%')) AND " +
            "p.createdAt BETWEEN :startDate AND :endDate")
    Page<PreRegister> searchByKeywordAndDateRange(
            @Param("keyword") String keyword,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}

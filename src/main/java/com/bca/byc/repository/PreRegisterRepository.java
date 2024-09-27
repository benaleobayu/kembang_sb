package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PreRegisterRepository extends JpaRepository<PreRegister, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT p FROM PreRegister p WHERE p.secureId = :id" )
    Optional<PreRegister> findBySecureId(@Param("id") String id);

    @Query("SELECT p FROM PreRegister p " +
            "WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.memberBankAccount) LIKE CONCAT('%', :keyword, '%')) AND " +
            "( :status IS NULL OR p.statusApproval = :status  ) AND " +
            "p.createdAt BETWEEN :startDate AND :endDate ")
    Page<PreRegister> searchByKeywordAndDateRange(
            @Param("keyword") String keyword,
            @Param("status") AdminApprovalStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);





    // validator
    boolean existsByMemberCin(String cin);


}

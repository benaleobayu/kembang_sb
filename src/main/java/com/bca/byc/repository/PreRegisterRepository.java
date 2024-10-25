package com.bca.byc.repository;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.export.PreRegisterExportResponse;
import com.bca.byc.model.projection.CmsGetIdFromSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PreRegisterRepository extends JpaRepository<PreRegister, Long> {

    @Query("SELECT p FROM PreRegister p WHERE p.secureId = :id" )
    Optional<PreRegister> findBySecureId(@Param("id") String id);

    // --- index pre-register ---
    @Query("SELECT p FROM PreRegister p " +
            "WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(p.memberBankAccount) LIKE CONCAT('%', :keyword, '%')) AND " +
            "( :status IS NULL OR p.statusApproval = :status  ) AND " +
            "p.createdAt BETWEEN :startDate AND :endDate AND " +
            "p.statusApproval IN :listStatus")
    Page<PreRegister> FindAllDataByKeywordAndStatus(
            @Param("listStatus") List<Integer> listStatus,
            @Param("keyword") String keyword,
            @Param("status") AdminApprovalStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);


    // --- index pre-register ---





    // validator
    boolean existsByEmail(String email);
    boolean existsByMemberCin(String cin);


    Set<PreRegister> findBySecureIdIn(Set<String> collect);


    // -- export --
    @Query("SELECT new com.bca.byc.model.export.PreRegisterExportResponse(" +
            "p.name, p.email, p.phone, p.accountType, " +
            "p.memberType, p.memberBankAccount, p.memberCin, " +
            "p.parentType, p.parentBankAccount, p.parentCin, " +
            "p.memberBirthdate, p.parentBirthdate, " +
            "branch.code, p.picName, " +
            "p.statusApproval , p.createdAt, user.name " +
            ") " +
            "FROM PreRegister p " +
            "LEFT JOIN p.branch branch " +
            "LEFT JOIN p.createdBy user " +
            "WHERE " +
            "p.createdAt BETWEEN :start AND :end AND " +
            "(:status IS NULL OR p.statusApproval = :status) ")
    List<PreRegisterExportResponse> findDataForExport(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end,
                                                      @Param("status") AdminApprovalStatus status);

    // -- export --

    @Query("SELECT pr.id AS id, pr.secureId AS secureId " +
            "FROM PreRegister pr " +
            "WHERE pr.secureId IN :ids")
    Set<CmsGetIdFromSecureIdProjection> findIdBySecureIdIn(Set<String> ids);
}

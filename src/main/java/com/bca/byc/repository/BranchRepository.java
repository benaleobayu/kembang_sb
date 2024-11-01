package com.bca.byc.repository;

import com.bca.byc.entity.Branch;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.export.BranchExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findBySecureId(String secureId);

    @Query("SELECT b FROM Branch b " +
            "WHERE (LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<Branch> findDataByKeyword(String keyword, Pageable pageable);

    @Query("SELECT b FROM Branch b " +
            "WHERE " +
            "(LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<Branch> findIdAndName(String keyword, Pageable pageable);

    // --- export ---
    @Query("SELECT new com.bca.byc.model.export.BranchExportResponse(" +
            "b.id, b.code, b.name, b.phone, l.name, b.isActive, b.createdAt, b.createdBy.name, b.updatedAt, b.createdBy.name) " +
            "FROM Branch b " +
            "JOIN b.location l")
    List<BranchExportResponse> findDataForExport();
}

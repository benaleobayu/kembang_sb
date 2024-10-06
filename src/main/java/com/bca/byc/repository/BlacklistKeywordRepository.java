package com.bca.byc.repository;

import com.bca.byc.entity.BlacklistKeyword;
import com.bca.byc.model.export.BlacklistKeywordExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlacklistKeywordRepository extends JpaRepository<BlacklistKeyword, Long> {

    Page<BlacklistKeyword> findByKeywordLikeIgnoreCase(String keyword, Pageable pageable);

    Optional<BlacklistKeyword> findBySecureId(String secureId);


    // --- export ---
    @Query("SELECT new com.bca.byc.model.export.BlacklistKeywordExportResponse(" +
            "bk.id, bk.keyword, bk.isActive, bk.orders, bk.createdAt, bk.createdBy.name, bk.updatedAt, bk.createdBy.name) " +
            "FROM BlacklistKeyword bk")
    List<BlacklistKeywordExportResponse> findDataForExport();
    // --- export ---
}

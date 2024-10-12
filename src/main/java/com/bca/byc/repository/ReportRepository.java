package com.bca.byc.repository;

import com.bca.byc.entity.Report;
import com.bca.byc.model.projection.ReportContentIndexProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query("SELECT " +
            "r.secureId AS id, " +
            "p.highlight AS highlight, " +
            "CASE WHEN pc.type = 'video' THEN pc.thumbnail ELSE pc.content END AS thumbnail, " +
            "p.description AS description, " +
            "STRING_AGG(t.name, ', ') AS tags, " +
            "u.appUserDetail.name AS creator, " +
            "r.status AS statusReport, " +
            "COUNT(r.id) AS totalReport, " +
            "r.createdAt AS lastReportAt " +
            "FROM Report r " +
            "LEFT JOIN r.post p " +
            "LEFT JOIN p.postContents pc " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN p.tags t " + // Menggabungkan dengan tags
            "WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "GROUP BY r.secureId, p.highlight, pc.type, pc.thumbnail, pc.content, p.description, u.appUserDetail.name, r.status, r.createdAt, r.id ")
    Page<ReportContentIndexProjection> getDataReportContent(@Param("keyword") String keyword, Pageable pageable);

}


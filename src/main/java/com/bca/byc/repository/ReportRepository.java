package com.bca.byc.repository;

import com.bca.byc.entity.Report;
import com.bca.byc.model.projection.ReportContentProjection;
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
            "p.description AS postDescription, " +
            "STRING_AGG(t.name, ', ') AS tags, " +
            "u.appUserDetail.name AS creator, " +
            "r.status AS statusReport, " +
            "COUNT(r.id) AS totalReport, " +
            "r.createdAt AS lastReportAt," +
            "p AS post, " +
            "channel.name AS channelName " +
            "FROM Report r " +
            "LEFT JOIN r.post p " +
            "LEFT JOIN p.postContents pc " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.channel channel " +
            "WHERE " +
            "(LOWER(p.description) LIKE LOWER(:keyword) OR " +
            "LOWER(r.secureId) LIKE LOWER(:reportId) ) " +
            "GROUP BY r.secureId, p.highlight, pc.type, pc.thumbnail, pc.content, p.description, u.appUserDetail.name, r.status, r.createdAt, r.id, p, channel.name ")
    Page<ReportContentProjection> getDataReportContent(@Param("reportId") String reportId, @Param("keyword") String keyword, Pageable pageable);

}


package com.bca.byc.repository;

import com.bca.byc.entity.Report;
import com.bca.byc.model.projection.ReportContentProjection;
import com.bca.byc.model.projection.ReportListDetailProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT " +
            "r AS report, " +
            "p AS post, " +
            "pc AS postContent, " + // Mengambil kolom tertentu dari postContent
            "u AS user, " +
            "u.appUserDetail AS userDetail, " +
            "u.appUserAttribute AS userAttribute, " +
            "t AS tag, " +
            "c AS channel " +
            "FROM Report r " +
            "LEFT JOIN r.post p " +
            "LEFT JOIN p.postContents pc " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN u.appUserDetail aud " +
            "LEFT JOIN u.appUserAttribute aua " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.channel c " +
            "WHERE " +
            "(LOWER(p.description) LIKE LOWER(:keyword) OR " +
            "LOWER(r.secureId) LIKE LOWER(:reportId)) AND " +
            "r.createdAt BETWEEN :startDate AND :endDate AND " +
            "(:reportStatus IS NULL OR r.status = :reportStatus) AND " +
            "(:reportType IS NULL OR r.type = :reportType)" +
            "GROUP BY p.id, r.id, u.id, u.appUserDetail.id, u.appUserAttribute.id, t.id, c.id , pc.id " +
            "ORDER BY r.createdAt DESC")
    Page<ReportContentProjection> getDataReportContent(@Param("reportId") String reportId,
                                                       @Param("keyword") String keyword,
                                                       Pageable pageable,
                                                       @Param("startDate") LocalDateTime start,
                                                       @Param("endDate") LocalDateTime end,
                                                       @Param("reportStatus") String reportStatus,
                                                       @Param("reportType") String reportType);



    @Query("SELECT " +
            "r.reporterUser.name AS reporterName," +
            "r.reason AS reason, " +
            "r.otherReason AS otherReason, " +
            "r.createdAt AS createdAt " +
            "FROM Report r " +
            "WHERE r.id = :id AND " +
            "(LOWER(r.reason) LIKE LOWER(:keyword) OR " +
            "LOWER(r.otherReason) LIKE LOWER(:keyword) )" )
    Page<ReportListDetailProjection> getListReportOnDetail(Long id, String keyword, Pageable pageable);


    Long countByPostId(Long id);

    Optional<Report> findBySecureId(String id);
}


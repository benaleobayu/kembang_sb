package com.bca.byc.repository;

import com.bca.byc.entity.Report;
import com.bca.byc.model.projection.ReportCommentIndexProjection;
import com.bca.byc.model.projection.ReportContentIndexProjection;
import com.bca.byc.model.projection.ReportListDetailProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // -- general --

    @Query("SELECT " +
            "r.reporterUser.name AS reporterName," +
            "r.reason AS reason, " +
            "r.otherReason AS otherReason, " +
            "r.createdAt AS createdAt " +
            "FROM Report r " +
            "WHERE " +
            "(:postId IS NULL OR r.post.id = :postId) AND " +
            "(:commentId IS NULL OR r.comment.id = :commentId) AND " +
            "(:replyId IS NULL OR r.commentReply.id = :replyId) AND " +
            "(:userId IS NULL OR r.reportedUser.id = :userId) AND " +
            "(LOWER(r.reason) LIKE LOWER(:keyword) OR " +
            "LOWER(r.otherReason) LIKE LOWER(:keyword) )")
    Page<ReportListDetailProjection> getListReportOnDetail(@Param("postId") Long postId,
                                                           @Param("commentId") Long commentId,
                                                           @Param("replyId") Long replyId,
                                                           @Param("userId") Long userId,
                                                           @Param("keyword") String keyword,
                                                           Pageable pageable);

    Long countByPostId(Long id);

    Optional<Report> findBySecureId(String id);

    // -- general --

    // ----- report content ----
    @Query("SELECT " +
            "r.secureId AS id, r.id AS index, p.highlight AS highlight, " +
            "CASE WHEN MAX(pc.type) = 'video' THEN MAX(pc.thumbnail) ELSE MAX(pc.content) END AS thumbnail, " +
            "p.description AS description, t AS tags, aud.name AS creator, ru.email AS reporterEmail, COUNT(r.id) AS totalReport, MAX(r.createdAt) AS lastReportAt, " +
            "c.name AS channelName,p AS post " +
            "FROM Post p " +
            "LEFT JOIN p.postContents pc " +
            "LEFT JOIN p.reports r " +
            "LEFT JOIN r.reporterUser ru " +
            "LEFT JOIN p.user u " +
            "LEFT JOIN u.appUserDetail aud " +
            "LEFT JOIN u.appUserAttribute aua " +
            "LEFT JOIN p.channel c " +
            "LEFT JOIN p.tags t " +
            "WHERE " +
            "(LOWER(p.description) LIKE LOWER(:keyword) OR " +
            "LOWER(r.secureId) LIKE LOWER(:reportId)) AND " +
            "r.createdAt BETWEEN :startDate AND :endDate AND " +
            "(:reportStatus IS NULL OR r.status = :reportStatus) AND " +
            "(:reportType IS NULL OR r.type = :reportType) AND " +
            "r.type = 'POST'" +
            "GROUP BY p.id, r.id, u.id, u.appUserDetail.id, u.appUserAttribute.id,  pc.id , t.id, ru.email, c.name " +
            "ORDER BY r.createdAt DESC")
    Page<ReportContentIndexProjection> getDataReportIndex(@Param("reportId") String reportId,
                                                          @Param("keyword") String keyword,
                                                          Pageable pageable,
                                                          @Param("startDate") LocalDateTime start,
                                                          @Param("endDate") LocalDateTime end,
                                                          @Param("reportStatus") String reportStatus,
                                                          @Param("reportType") String reportType);

    @Query("SELECT " +
            "r.secureId AS id, " +
            "r.id AS index, " +
            "CASE WHEN c.id IS NULL THEN " +
            "   CASE WHEN crpc.type = 'video' THEN crpc.thumbnail " +
            "   ELSE crpc.content END " +
            "ELSE " +
            "   CASE WHEN rpc.type = 'video' THEN rpc.thumbnail " +
            "   ELSE rpc.content END END AS thumbnail, " +
            "CASE WHEN c.id IS NULL THEN cr.comment " +
            "ELSE c.comment END AS comment, " +
            "CASE WHEN c.id IS NULL THEN craud.name " +
            "ELSE caud.name END AS commentOwner, " +
            "r.status AS statusReport, " +
            "CASE WHEN c.id IS NULL THEN COUNT(cr.id) ELSE COUNT(c.id) END AS totalReports, " +
            "MAX(r.createdAt) AS lastReport, " +
            "r AS report " +
            "FROM Report r " +
            "LEFT JOIN r.comment c " +
            "LEFT JOIN r.commentReply cr " +
            "LEFT JOIN c.post.postContents rpc " +
            "LEFT JOIN cr.parentComment.post.postContents crpc " +
            "LEFT JOIN c.user.appUserDetail caud " +
            "LEFT JOIN cr.user.appUserDetail craud " +
            "WHERE " +
            "(LOWER(c.comment) LIKE LOWER(:keyword) OR " +
            "LOWER(cr.comment) LIKE LOWER(:keyword) OR " +
            "LOWER(r.secureId) LIKE LOWER(:reportId) ) AND " +
            "r.createdAt BETWEEN :startDate AND :endDate AND " +
            "(:reportStatus IS NULL OR r.status = :reportStatus) AND " +
            "(:reportType IS NULL OR r.type = :reportType) " +
            "GROUP BY r, c, cr, rpc, crpc, caud, craud, c.id, cr.comment, craud.name, caud.name ")
    Page<ReportCommentIndexProjection> getDataReportCommentIndex(@Param("reportId") String reportId,
                                                                 @Param("keyword") String keyword,
                                                                 Pageable pageable,
                                                                 @Param("startDate") LocalDateTime start,
                                                                 @Param("endDate") LocalDateTime end,
                                                                 @Param("reportStatus") String reportStatus,
                                                                 @Param("reportType") String reportType);

    // ----- report content ----


}


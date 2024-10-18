package com.bca.byc.repository;

import com.bca.byc.entity.ReasonReport;
import com.bca.byc.model.projection.ReasonReportProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReasonReportRepository extends JpaRepository<ReasonReport, Long> {

    Optional<ReasonReport> findBySecureId(String secureId);

    @Query("SELECT new com.bca.byc.model.projection.ReasonReportProjection(" +
            " rr.secureId, rr.icon, rr.name, rr.orders, rr.isActive, rr.isRequired, rr.createdAt, rrc.name, rr.updatedAt, rru.name) " +
            "FROM ReasonReport rr " +
            "LEFT JOIN rr.updatedBy rru " +
            "LEFT JOIN rr.createdBy rrc " +
            "WHERE " +
            "(LOWER(rr.name) LIKE (:keyword) ) AND " +
            "rr.isActive = true AND rr.isDeleted = false")
    Page<ReasonReportProjection> findDataReasonReportIndex(String keyword, Pageable pageable);

    @Query("SELECT new com.bca.byc.model.projection.ReasonReportProjection(" +
            " rr.secureId, rr.icon, rr.name, rr.orders, rr.isActive, rr.isRequired, rr.createdAt, rrc.name, rr.updatedAt, rru.name) " +
            "FROM ReasonReport rr " +
            "LEFT JOIN rr.updatedBy rru " +
            "LEFT JOIN rr.createdBy rrc " +
            "WHERE " +
            "rr.isActive = true AND rr.isDeleted = false")
    List<ReasonReportProjection> findDataForPublic();
}
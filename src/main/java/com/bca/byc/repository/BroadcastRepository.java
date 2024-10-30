package com.bca.byc.repository;

import com.bca.byc.entity.BroadcastManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BroadcastRepository extends JpaRepository<BroadcastManagement, Long> {


    @Query("""
            SELECT b
            FROM BroadcastManagement b
            WHERE
            (LOWER(b.title) LIKE LOWER(:keyword) OR
            LOWER(b.message) LIKE LOWER(:keyword) OR
            LOWER(b.status) LIKE LOWER(:keyword)) AND
            (:status IS NULL OR b.status = :status) AND
            b.createdAt between :start AND :end
            """)
    Page<BroadcastManagement> findDataByKeyword(@Param("keyword") String keyword,
                                                @Param("status") String status,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end,
                                                Pageable pageable);
}
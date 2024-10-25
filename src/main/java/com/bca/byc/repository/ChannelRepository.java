package com.bca.byc.repository;

import com.bca.byc.entity.Channel;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query("""
SELECT c FROM Channel c
WHERE
(LOWER(c.name) LIKE(:keyword) OR
LOWER(c.description) LIKE(:keyword)) AND
c.createdAt BETWEEN :startDate AND :endDate AND
(:status IS NULL OR c.isActive = :status)
""")
    Page<Channel> findByNameLikeIgnoreCaseOrderByOrders(@Param("keyword") String keyword,
                                                        Pageable pageable,
                                                        @Param("startDate") LocalDateTime start,
                                                        @Param("endDate") LocalDateTime end,
                                                        @Param("status") Boolean status);

    @Query("SELECT c.secureId AS secureId, c.name AS name FROM Channel c " +
            "WHERE " +
            "(LOWER(c.name) LIKE LOWER(:keyword) ) AND " +
            "c.isActive = true ")
    Page<CastSecureIdAndNameProjection> findIdAndName(String keyword, Pageable pageable);

    @Query("SELECT c FROM Channel c WHERE c.secureId = :channelId ")
    Optional<Channel> findBySecureId(String channelId);

    @Query("SELECT c FROM Channel c WHERE c.secureId = :channelId ")
    Optional<IdSecureIdProjection> findByIdAndSecureId(@Param("channelId") String channelId);
}

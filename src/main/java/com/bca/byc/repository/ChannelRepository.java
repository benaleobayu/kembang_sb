package com.bca.byc.repository;

import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Location;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Page<Channel> findByNameLikeIgnoreCaseOrderByOrders(String keyword, Pageable pageable);

    @Query("SELECT c.secureId AS secureId, c.name AS name FROM Channel c " +
            "WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<CastSecureIdAndNameProjection> findIdAndName(String keyword, Pageable pageable);

    @Query("SELECT c FROM Channel c WHERE c.secureId = :channelId ")
    Optional<Channel> findBySecureId(String channelId);
}

package com.bca.byc.repository;

import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Location;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Page<Channel> findByNameLikeIgnoreCaseOrderByOrders(String keyword, Pageable pageable);

    @Query("SELECT l.secureId AS secureId, l.name AS name FROM Location l " +
            "WHERE " +
            "(LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<CastSecureIdAndNameProjection> findIdAndName(String keyword, Pageable pageable);
}

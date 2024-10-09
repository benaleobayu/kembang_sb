package com.bca.byc.repository;

import com.bca.byc.entity.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Page<Channel> findByNameLikeIgnoreCaseOrderByOrders(String keyword, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.Chanel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChanelRepository extends JpaRepository<Chanel, Long> {

    Page<Chanel> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    Page<Faq> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {
    Page<FaqCategory> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}

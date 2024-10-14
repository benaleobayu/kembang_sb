package com.bca.byc.repository;

import com.bca.byc.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    @Query("SELECT f FROM Faq f " +
            "WHERE " +
            "(LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "f.isDeleted = false AND " +
            "f.isActive = true AND " +
            "f.faqCategoryId.id = :categoryId")
    Page<Faq> getFaqItemIndex(@Param("keyword") String keyword, Pageable pageable, @Param("categoryId") Long categoryId);
}

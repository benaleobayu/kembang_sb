package com.bca.byc.repository;

import com.bca.byc.entity.Faq;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    @Query("SELECT f FROM Faq f " +
            "WHERE " +
            "(LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%') )) AND " +
            "f.isDeleted = false AND " +
            "f.faqCategoryId.id = :categoryId")
    Page<Faq> getFaqItemIndex(@Param("keyword") String keyword, Pageable pageable, @Param("categoryId") Long categoryId);

    @Query("SELECT f.id, f.secureId FROM Faq f WHERE f.secureId = :secureId")
    Optional<IdSecureIdProjection> findByIdSecureId(@Param("secureId") String secureId);

    Optional<Faq> findBySecureId(String secureId);
}

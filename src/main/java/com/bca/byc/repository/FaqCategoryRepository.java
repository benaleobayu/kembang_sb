package com.bca.byc.repository;

import com.bca.byc.entity.FaqCategory;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {

    Page<FaqCategory> findByNameLikeIgnoreCase(String keyword, Pageable pageable);

    Optional<FaqCategory> findBySecureId(String secureId);

    @Query("SELECT fc FROM FaqCategory fc WHERE fc.isDeleted = false AND fc.secureId = :id")
    Optional<IdSecureIdProjection> findByIdAndSecureId(@Param("id") String id);

    @Query("SELECT fc " +
            "FROM FaqCategory fc " +
            "LEFT JOIN fc.faqs f " +
            "WHERE fc.isActive = true AND fc.isDeleted = false AND " +
            "f.isActive = true AND f.isDeleted = false " +
            "ORDER BY fc.orders ASC")
    List<FaqCategory> findAllAndStatusIsActive();
}

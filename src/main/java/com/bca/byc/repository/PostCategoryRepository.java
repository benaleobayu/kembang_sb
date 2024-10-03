package com.bca.byc.repository;

import com.bca.byc.entity.PostCategory;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    @Query("SELECT d FROM PostCategory d " +
            "LEFT JOIN AppAdmin a ON a.id = d.createdBy.id " +
            "WHERE " +
            "(LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%') ) OR " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) ) AND " +
            "d.isDeleted = false")
    Page<PostCategory> findByNameLikeIgnoreCase(String keyword, Pageable pageable);

    Optional<IdSecureIdProjection> findBySecureId(String postCategoryId);
}

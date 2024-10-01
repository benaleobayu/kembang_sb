package com.bca.byc.repository;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpectItemRepository extends JpaRepository<ExpectItem, Long> {

    @Query("select e from ExpectItem e where e.secureId = :id")
    Optional<IdSecureIdProjection> findBySecureId(@Param("id") String s);

    @Query("select e from ExpectItem e where e.expectCategory = :parentId")
    List<ExpectItem> findByParentId(ExpectCategory parentId);

    @Query("SELECT e FROM ExpectItem e " +
            "WHERE e.name = :name AND " +
            "(e.expectCategory IS NULL OR e.expectCategory.id = :id)")
    Optional<ExpectItem> findByNameSubCategory(@Param("id") Long id,@Param("name") String name);

    @Query("SELECT e FROM ExpectItem e " +
            "WHERE " +
            "(LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<ExpectItem> findIdAndName(String keyword, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.model.data.InputAttributeResponse;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.secureId = :secureId AND bc.isDeleted = false" )
    Optional<BusinessCategory> findBySecureId(String secureId);

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.parentId IS NULL")
    List<BusinessCategory> findByParentIdIsNull();

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.parentId IS NOT NULL")
    List<BusinessCategory> findByParentIdIsNotNull();

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.parentId = :parentId")
    List<BusinessCategory> findByParentId(BusinessCategory parentId);

    @Query("SELECT bc " +
            "FROM BusinessCategory bc " +
            "WHERE " +
            "(LOWER(bc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ) AND " +
            "bc.isDeleted = false AND " +
            "bc.parentId IS NULL ")
    Page<BusinessCategory> findDataByKeyword(String keyword, Pageable pageable);

    @Query("SELECT bc " +
            "FROM BusinessCategory bc " +
            "WHERE " +
            "(LOWER(bc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ) AND " +
            "bc.isDeleted = false AND " +
            "bc.parentId IS NOT NULL ")
    Page<BusinessCategory> findDataItemByKeyword(String keyword, Pageable pageable);

    @Query("SELECT bc FROM BusinessCategory bc " +
            "WHERE bc.isDeleted = false AND " +
            "bc.name = :name AND " +
            "(bc.parentId IS NULL OR bc.parentId.id = :parentId)")
    Optional<BusinessCategory> findByNameSubCategory(@Param("parentId") Long savedDataId, @Param("name") String name);

    @Query("SELECT bc FROM BusinessCategory bc " +
            "WHERE " +
            "(LOWER(bc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "bc.parentId IS NOT NULL ")
    Page<BusinessCategory> findIdAndName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT bc FROM BusinessCategory bc " +
            "WHERE bc.isDeleted = false AND " +
            "bc.secureId = :id")
    Optional<IdSecureIdProjection> findByIdAndSecureId(@Param("id") String childCategoryId);
}

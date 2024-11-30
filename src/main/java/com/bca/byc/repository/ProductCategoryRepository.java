package com.bca.byc.repository;

import com.bca.byc.entity.ProductCategory;
import com.bca.byc.model.export.SimpleExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("""
            SELECT pc FROM ProductCategory pc
            WHERE LOWER(pc.name) LIKE LOWER(:keyword)
            """)
    Page<ProductCategory> listDataProductCategory(String keyword, Pageable pageable);

    @Query("""
            SELECT new com.bca.byc.model.export.SimpleExportResponse(
                    pc.id, pc.name, pc.isActive, pc.createdAt, cr.name, pc.updatedAt, cu.name)
            FROM ProductCategory pc
            LEFT JOIN AppAdmin cr ON pc.createdBy = cr.id
            LEFT JOIN AppAdmin cu ON pc.updatedBy = cu.id
            """)
    List<SimpleExportResponse> findDataForExport();
}
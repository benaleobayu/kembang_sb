package com.bca.byc.repository;

import com.bca.byc.entity.Product;
import com.bca.byc.model.export.SimpleExportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
            SELECT p FROM Product p
            WHERE LOWER(p.name) LIKE LOWER(:keyword)
            """)
    Page<Product> listDataProduct(String keyword, Pageable pageable);

    @Query("""
            SELECT new com.bca.byc.model.export.SimpleExportResponse( p.id, p.name, p.isActive, p.createdAt, cr.name, p.updatedAt, cu.name)
            FROM Product p
            LEFT JOIN AppAdmin cr ON p.createdBy = cr.id
            LEFT JOIN AppAdmin cu ON p.updatedBy = cu.id
            """)
    List<SimpleExportResponse> findDataForExport();
}
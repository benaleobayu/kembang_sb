package com.kembang.repository;

import com.kembang.entity.Product;
import com.kembang.model.dto.SavedLongAndStringValue;
import com.kembang.model.dto.SavedStringAndIntegerValue;
import com.kembang.model.export.SimpleExportResponse;
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
            WHERE
            (LOWER(p.name) LIKE LOWER(:keyword) OR
            LOWER(p.code) LIKE LOWER(:keyword))
            """)
    Page<Product> listDataProduct(String keyword, Pageable pageable);

    @Query("""
            SELECT new com.kembang.model.export.SimpleExportResponse( p.id, p.name, p.isActive, p.createdAt, cr.name, p.updatedAt, cu.name)
            FROM Product p
            LEFT JOIN AppAdmin cr ON p.createdBy = cr.id
            LEFT JOIN AppAdmin cu ON p.updatedBy = cu.id
            """)
    List<SimpleExportResponse> findDataForExport();

    @Query("""
            SELECT p FROM Product p
            WHERE p.secureId IN (:productIdList)
            """)
    List<Product> findAllByIdIn(List<String> productIdList);

    @Query("""
            SELECT new com.kembang.model.dto.SavedStringAndIntegerValue(p.secureId, p.price)
            FROM Product p
            WHERE p.secureId IN (:idList)
            """)
    List<SavedStringAndIntegerValue> listDataProductPriceByProductSecureId(List<String> idList);

    @Query("""
SELECT new com.kembang.model.dto.SavedLongAndStringValue(o.id, ohp.secureId)
FROM OrderHasProduct ohp
JOIN ohp.order o
WHERE o.id IN (:idList)
""")
    List<SavedLongAndStringValue> listOrderHasProductSecureIdByOrderId(List<Long> idList);
}
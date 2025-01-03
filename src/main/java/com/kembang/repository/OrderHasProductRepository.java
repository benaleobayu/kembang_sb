package com.kembang.repository;

import com.kembang.entity.OrderHasProduct;
import com.kembang.model.projection.DataOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderHasProductRepository extends JpaRepository<OrderHasProduct, Long> {

    @Query("""
            SELECT new com.kembang.model.projection.DataOrderResponse(
                ohp.secureId, p.secureId, p.name, ohp.quantity, ohp.totalPrice
            )
            FROM OrderHasProduct ohp
            JOIN ohp.product p
            WHERE ohp.order.id = :id
            """)
    List<DataOrderResponse> listDataOrderByOrderId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderHasProduct ohp WHERE ohp.secureId IN (:filteredOrderId)")
    void deleteBySecureIdIn(List<String> filteredOrderId);
}
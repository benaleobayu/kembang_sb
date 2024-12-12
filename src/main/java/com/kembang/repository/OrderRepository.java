package com.kembang.repository;

import com.kembang.entity.Order;
import com.kembang.model.projection.OrderIndexProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT new com.kembang.model.projection.OrderIndexProjection(
                o.id, o.secureId, o.forwardName, o.forwardAddress,
                u.secureId, u.name, u.address, u.phone, u.location,
                o.orderDate, o.deliveryDate, o.driverName, o.route,
                o.isPaid, o.isActive, o.createdAt)
            FROM Order o
            LEFT JOIN o.customer u
            WHERE
            LOWER(u.name) LIKE LOWER(:keyword) AND
            o.deliveryDate BETWEEN :startDate AND :endDate AND
            (:location IS NULL OR u.location = :location) AND
            (:route IS NULL OR o.route = :route)
            """)
    Page<OrderIndexProjection> listDataOrder(@Param("keyword") String keyword,
                                             Pageable pageable,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("location") String location,
                                             @Param("route") Integer route);

    @Query("""
            SELECT new com.kembang.model.projection.OrderIndexProjection(
                o.id, o.secureId, o.forwardName, o.forwardAddress,
                u.secureId, u.name, u.address, u.phone, u.location,
                o.orderDate, o.deliveryDate, o.driverName, o.route,
                o.isPaid, o.isActive, o.createdAt)
            FROM Order o
            JOIN o.customer u
            WHERE o.secureId = :id
            """)
    OrderIndexProjection findDataBySecureIdProjection(String id);
}
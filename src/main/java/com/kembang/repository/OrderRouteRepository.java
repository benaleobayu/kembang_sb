package com.kembang.repository;

import com.kembang.entity.OrderRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRouteRepository extends JpaRepository<OrderRoute, Long> {

    OrderRoute findByRouteAndDate(Integer routeId, LocalDate routeDate);
}
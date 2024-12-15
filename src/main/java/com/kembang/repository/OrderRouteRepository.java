package com.kembang.repository;

import com.kembang.entity.OrderRoute;
import com.kembang.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderRouteRepository extends JpaRepository<OrderRoute, Long> {

    OrderRoute findByRouteAndDate(Integer routeId, LocalDate routeDate);

    @Query("SELECT new com.kembang.model.projection.IdSecureIdProjection(o.id, o.secureId) FROM OrderRoute o WHERE o.secureId = :s")
    Optional<IdSecureIdProjection> findIdBySecureId(String s);

    @Query("""
            SELECT or FROM OrderRoute or
            WHERE
            or.date BETWEEN :startDate AND :endDate
            """)
    Page<OrderRoute> listDataOrderRoute(Pageable pageable, LocalDate startDate, LocalDate endDate);
}
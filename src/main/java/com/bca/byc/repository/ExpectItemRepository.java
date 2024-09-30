package com.bca.byc.repository;

import com.bca.byc.entity.ExpectItem;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpectItemRepository extends JpaRepository<ExpectItem, Long> {

    @Query("select e from ExpectItem e where e.secureId = :id")
    Optional<IdSecureIdProjection> findBySecureId(@Param("id") String s);
}

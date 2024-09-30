package com.bca.byc.repository;

import com.bca.byc.entity.Location;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findById(Long id);

    @Query("select l from Location l where l.secureId = :id")
    Optional<IdSecureIdProjection> findBySecureId(@Param("id") String id);
}

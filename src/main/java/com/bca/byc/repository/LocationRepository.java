package com.bca.byc.repository;

import com.bca.byc.entity.Location;
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
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findById(Long id);

    @Query("SELECT l FROM Location l WHERE l.secureId = :id")
    Optional<IdSecureIdProjection> findBySecureId(@Param("id") String id);

    @Query("SELECT l FROM Location l WHERE l.isDeleted = false AND l.secureId = :id")
    Optional<IdSecureIdProjection> findByIdAndSecureId(@Param("id") String id);

    @Query("SELECT l FROM Location l WHERE l.isActive = true AND l.isDeleted = false ORDER BY l.name ASC")
    List<Location> findAllAndOrderByName();

    Page<Location> findByNameLikeIgnoreCase(String keyword, Pageable pageable);
}

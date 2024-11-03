package com.bca.byc.repository;

import com.bca.byc.entity.Location;
import com.bca.byc.model.export.LocationExportResponse;
import com.bca.byc.model.projection.CastIdBySecureIdProjection;
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


    // --- input attribute ---
    @Query("SELECT l FROM Location l " +
            "WHERE " +
            "(LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.province) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(l.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
    Page<Location> findIdAndName(String keyword, Pageable pageable);
    // --- input attribute ---

    // --- export ---
    @Query("SELECT new com.bca.byc.model.export.LocationExportResponse(" +
            "l.id, l.province, l.name, l.isActive, l.orders, l.createdAt, l.createdBy, l.updatedAt, l.updatedBy) " +
            "FROM Location l")
    List<LocationExportResponse> findDataForExport();

    // -- projection ---

    @Query("SELECT l.id, l.secureId FROM Location l WHERE l.secureId = :location")
    CastIdBySecureIdProjection findIdBySecureId(@Param("location") String location);
}

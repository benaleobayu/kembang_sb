package com.bca.byc.repository;

import com.bca.byc.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Override
    Optional<Location> findById(Long id);
}

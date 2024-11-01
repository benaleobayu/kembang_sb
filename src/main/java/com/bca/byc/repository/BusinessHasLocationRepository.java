package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessHasLocation;
import com.bca.byc.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHasLocationRepository extends JpaRepository<BusinessHasLocation, Long> {

    // Method to delete all BusinessHasLocation entries by Business
    void deleteByBusiness(Business business);

    @Query("SELECT bhl FROM BusinessHasLocation  bhl WHERE bhl.business.id = :id")
    List<Location> findLocationByBusinessId(@Param("id") Long id);
}

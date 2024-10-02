package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessHasLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHasLocationRepository extends JpaRepository<BusinessHasLocation, Long> {

    // Method to delete all BusinessHasLocation entries by Business
    void deleteByBusiness(Business business);
}

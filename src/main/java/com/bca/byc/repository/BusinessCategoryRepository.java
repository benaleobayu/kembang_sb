package com.bca.byc.repository;

import com.bca.byc.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {

    Optional<BusinessCategory> findById(Long Id);
}

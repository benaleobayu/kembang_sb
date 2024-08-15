package com.bca.byc.repository;

import com.bca.byc.entity.BusinessHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHasCategoryRepository extends JpaRepository<BusinessHasCategory, Long> {
}

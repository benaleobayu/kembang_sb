package com.bca.byc.repository;


import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.BusinessHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHasCategoryRepository extends JpaRepository<BusinessHasCategory, Long> {
    void deleteByBusiness(Business business);

    boolean existsByBusinessCategoryParent(BusinessCategory data);

    boolean existsByBusinessCategoryChild(BusinessCategory data);

    @Query("SELECT b FROM BusinessHasCategory b WHERE b.business.id = :id")
    List<BusinessHasCategory> findCategoryByBusinessId(@Param("id") Long id);
}

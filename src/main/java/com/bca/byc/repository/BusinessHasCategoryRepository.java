package com.bca.byc.repository;


import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.BusinessHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessHasCategoryRepository extends JpaRepository<BusinessHasCategory, Long> {
        void deleteByBusiness(Business business);

        boolean existsByBusinessCategoryParent(BusinessCategory data);

        boolean existsByBusinessCategoryChild(BusinessCategory data);
}

package com.bca.byc.repository;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, Long> {

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.parentId IS NULL")
    List<BusinessCategory> findByParentIdIsNull();

    @Query("SELECT bc FROM BusinessCategory bc WHERE bc.parentId IS NOT NULL")
    List<BusinessCategory> findByParentIdIsNotNull();




}

package com.bca.byc.repository;

import com.bca.byc.entity.ExpectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpectCategoryRepository extends JpaRepository<ExpectCategory, Long> {

}
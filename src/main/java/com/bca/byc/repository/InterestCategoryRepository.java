package com.bca.byc.repository;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.InterestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

}

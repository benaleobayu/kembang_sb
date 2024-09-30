package com.bca.byc.repository;

import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.model.projection.ExpectCategoryProjection;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpectCategoryRepository extends JpaRepository<ExpectCategory, Long> {

    @Query("select e from ExpectCategory e where e.secureId = :id")
    Optional<IdSecureIdProjection> findBySecureId(@Param("id") String expectCategoryId);
}
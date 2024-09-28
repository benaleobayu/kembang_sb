package com.bca.byc.repository;

import com.bca.byc.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findBySecureId(String secureId);

    @Query("SELECT b FROM Branch b " +
            "WHERE (LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')) )")
    Page<Branch> findDataByKeyword(String keyword, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.Account;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.secureId = :id")
    Optional<IdSecureIdProjection> findByIdAndSecureId(@Param("id") String accountId);

    @Query("SELECT a.secureId AS secureId, a.name AS name FROM Account a " +
            "WHERE LOWER(a.name) LIKE LOWER(:keyword) AND a.isActive = true AND a.isDeleted = false")
    Page<CastSecureIdAndNameProjection> findSecureIdAndName(String keyword, Pageable pageable);

    boolean existsBySecureId(String accountId);

    @Query("""
            SELECT a
            FROM Account a
            WHERE (LOWER(a.name) LIKE(:keyword))
            """)
    Page<Account> findByKeywordAccountIndex(String keyword, Pageable pageable);
}
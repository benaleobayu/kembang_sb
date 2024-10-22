package com.bca.byc.repository;

import com.bca.byc.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
            SELECT a
            FROM Account a
            WHERE (LOWER(a.name) LIKE(:keyword))
            """)
    Page<Account> findByKeywordAccountIndex(String keyword, Pageable pageable);
}
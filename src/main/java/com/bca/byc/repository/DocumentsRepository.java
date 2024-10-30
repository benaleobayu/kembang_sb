package com.bca.byc.repository;

import com.bca.byc.entity.Documents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {

    @Query("""
            SELECT d FROM Documents d
            WHERE (LOWER(d.name) LIKE LOWER(:keyword) )
            """)
    Page<Documents> findDataByKeyword(String keyword, Pageable pageable);
}
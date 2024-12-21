package com.kembang.repository;

import com.kembang.entity.Documents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {

    Optional<Documents> findByIdentity(String identity);

    @Query("""
            SELECT d FROM Documents d
            WHERE (LOWER(d.name) LIKE LOWER(:keyword) )
            """)
    Page<Documents> findDataByKeyword(String keyword, Pageable pageable);

    @Query("SELECT d.urlFile FROM Documents d WHERE d.identity = :identity")
    Optional<String> findUrlFileByIdentity(String identity);
}
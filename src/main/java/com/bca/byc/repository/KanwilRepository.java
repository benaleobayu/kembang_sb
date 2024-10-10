package com.bca.byc.repository;

import com.bca.byc.entity.Kanwil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KanwilRepository extends JpaRepository<Kanwil, Long> {
    Optional<Kanwil> findBySecureId(String secureId);

    @Query("select k from Kanwil k " +
            "WHERE " +
            "(LOWER(k.name) LIKE LOWER(CONCAT('%', :keyword, '%') ) )")
    Page<Kanwil> findDataByKeyword(String keyword, Pageable pageable);

    Page<Kanwil> findIdAndName(String keyword, Pageable pageable);
}

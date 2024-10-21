package com.bca.byc.repository;

import com.bca.byc.entity.AppUserRequestContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestContactRepository extends JpaRepository<AppUserRequestContact, Long> {

    Optional<AppUserRequestContact> findById(Long id);

    @Query("SELECT arc FROM AppUserRequestContact arc " +
            "LEFT JOIN arc.user u " +
            "ORDER BY u.name ASC")
    List<AppUserRequestContact> findAllAndOrderByName();

    // --- input attribute ---
    @Query("SELECT arc FROM AppUserRequestContact arc " +
            "LEFT JOIN arc.user u")
    Page<AppUserRequestContact> findIdAndName(@Param("keyword") String keyword, Pageable pageable);
    // --- input attribute ---

}

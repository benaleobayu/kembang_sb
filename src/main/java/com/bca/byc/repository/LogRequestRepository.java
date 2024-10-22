package com.bca.byc.repository;

import com.bca.byc.entity.AppUserRequestContact;
import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.entity.LogRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRequestRepository extends JpaRepository<LogRequest, Long> {

    Optional<LogRequest> findById(Long id);

    @Query("SELECT lr FROM LogRequest lr")
    List<LogRequest> findAllAndOrderByName();

    // --- input attribute ---
    @Query("SELECT lr " +
            "FROM LogRequest lr " +
            "WHERE lr.modelableId = :id " +
            "AND lr.note LIKE %:keyword%")
    Page<LogRequest> listLogRequestByModelableId(@Param("id") String id,
                                                 @Param("keyword") String keyword,
                                                 Pageable pageable);
    // --- input attribute ---


}

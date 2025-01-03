package com.kembang.repository;

import com.kembang.entity.LogRequest;
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

    @Query("SELECT l FROM LogRequest l WHERE l.modelId = :id AND l.modelType = :preRegister AND l.logTo = :logTo ORDER BY l.createdAt DESC")
    LogRequest findMessageOnPreRegisterRejected(@Param("id") Long id, @Param("preRegister") String preRegister, @Param("logTo") String logTo);


    @Query("SELECT lr FROM LogRequest lr")
    List<LogRequest> findAllAndOrderByName();

    // --- input attribute ---
    @Query("SELECT lr " +
            "FROM LogRequest lr " +
            "WHERE lr.secureId = :id " +
            "AND (LOWER(lr.note) LIKE LOWER(:keyword)) ")
    Page<LogRequest> listLogRequestByModelableId(@Param("id") String id,
                                                 @Param("keyword") String keyword,
                                                 Pageable pageable);

    // --- input attribute ---


}

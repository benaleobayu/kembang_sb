package com.bca.byc.repository;

import com.bca.byc.entity.CommentReply;
import com.bca.byc.model.projection.IdSecureIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {

    @Query("SELECT c FROM CommentReply c WHERE c.secureId = :secureId")
    Optional<CommentReply> findBySecureId(@Param("secureId") String secureId);

    @Query("SELECT c FROM CommentReply c WHERE c.secureId = :secureId")
    Optional<IdSecureIdProjection> findByIdSecureId(@Param("secureId") String secureId);
}

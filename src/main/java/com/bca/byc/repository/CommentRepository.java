package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN CommentReply cr ON c.id = cr.parentComment.id " +
            "WHERE c.post.secureId = :postId")
    Page<Comment> findListDataComment(@Param("postId") String postId, Pageable pageable);

    Boolean existsByPostAndPostUser(Post post, AppUser user);

    @Query("SELECT c FROM Comment c WHERE c.secureId = :secureId")
    Optional<Comment> findBySecureId(@Param("secureId") String secureId);
}

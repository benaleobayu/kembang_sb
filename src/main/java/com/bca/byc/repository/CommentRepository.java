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
            "WHERE c.comment LIKE %:keyword% AND " +
            "c.post.secureId = :postId")
    Page<Comment> findListDataComment(@Param("postId") String postId, @Param("keyword") String keyword, Pageable pageable);

    Boolean existsByPostAndPostUser(Post post, AppUser user);

    Optional<Comment> findBySecureId(String secureId);
}

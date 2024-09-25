package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "WHERE c.content LIKE %:keyword% AND " +
            "c.post.id = :postId")
    Page<Comment> findListDataComment(@Param("postId") Long postId, @Param("keyword") String keyword, Pageable pageable);

    Boolean existsByPostAndPostUser(Post post, AppUser user);
}

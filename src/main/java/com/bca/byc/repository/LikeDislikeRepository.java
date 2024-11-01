package com.bca.byc.repository;

import com.bca.byc.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    LikeDislike findByPostAndUser(Post post, AppUser user);

    LikeDislike findByCommentAndUser(Comment comment, AppUser user);

    LikeDislike findByCommentReplyAndUser(CommentReply comment, AppUser user);

    @Query("SELECT ld FROM LikeDislike ld WHERE ld.user.secureId = :userId AND ld.post IS NOT NULL ")
    Page<LikeDislike> findLikesByUserId(@Param("userId") String userId, Pageable pageable);


    // ---- count like and dislike by id ---
    Integer countByPostId(Long id);

    Integer countByCommentId(Long id);

    Integer countByCommentReplyId(Long id);


    // ---- count like and dislike by id ---
    // ---- get boolean like and dislike ---

    Optional<LikeDislike> findByPostIdAndUserId(Long id, Long userId);

    Optional<LikeDislike> findByCommentIdAndUserId(Long commentId, Long userId);

    Optional<LikeDislike> findByCommentReplyIdAndUserId(Long commentReplyId, Long userId);


    // ---- get boolean like and dislike ---

    // -- delete ---
    @Modifying
    @Query("DELETE FROM LikeDislike ld WHERE ld.post = :post")
    void deletePostLikeByPost(@Param("post") Post data);


}

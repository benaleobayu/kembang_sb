package com.bca.byc.repository;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.model.projection.IdSecureIdProjection;
import com.bca.byc.model.projection.PostCommentActivityProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c " +
            "LEFT JOIN CommentReply cr ON c.id = cr.parentComment.id " +
            "WHERE c.post.secureId = :postId")
    Page<Comment> findListDataComment(@Param("postId") String postId, Pageable pageable);

    Boolean existsByPostAndPostUser(Post post, AppUser user);

    @Query("SELECT c FROM Comment c WHERE c.secureId = :secureId")
    Optional<Comment> findBySecureId(@Param("secureId") String secureId);

    @Query("SELECT " +
            "c AS comment, " +
            "cr AS commentReply, " +
            "u AS commentUser, " +
            "ur AS replyUser, " +
            "p AS post " +
            "FROM Post p " +
            "JOIN p.comments c " +
            "LEFT JOIN c.commentReply cr " +
            "LEFT JOIN AppUser u ON c.user.id = u.id " +
            "LEFT JOIN AppUserDetail audu ON audu.id = u.appUserDetail.id " +
            "LEFT JOIN AppUser ur ON cr.user.id = ur.id " +
            "LEFT JOIN AppUserDetail audur ON audur.id = ur.appUserDetail.id " +
            "WHERE u.id = :userId OR ur.id = :userId " )
    Page<PostCommentActivityProjection> findAllActivityCommentByUser(@Param("userId") Long userId, Pageable pageable);

    // -- post comment activity --
    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    Page<Comment> findAllCommentUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.user.id = :userId")
    List<Comment> findCommentsByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("SELECT r FROM CommentReply r WHERE r.parentComment.id = :commentId AND r.user.id = :userId")
    List<CommentReply> findRepliesByCommentAndUser(@Param("commentId") Long commentId, @Param("userId") Long userId);
    // -- post comment activity --

    Integer countByPostId(Long id);

    // ------------------------- Projection -----------------------

    @Query("SELECT c FROM Comment c WHERE c.post.secureId = :postId")
    Optional<IdSecureIdProjection> findByIdSecureId(@Param("postId") String postId);

    // ------------------------- Projection -----------------------

}

package com.bca.byc.repository;

import com.bca.byc.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    LikeDislike findByPostAndUser(Post post, AppUser user);

    LikeDislike findByCommentAndUser(Comment comment, AppUser user);

    LikeDislike findByCommentReplyAndUser(CommentReply comment, AppUser user);

    @Query("SELECT ld FROM LikeDislike ld WHERE ld.user.secureId = :userId")
    Page<LikeDislike> findLikesByUserId(@Param("userId") String userId, Pageable pageable);
}

package com.bca.byc.repository;

import com.bca.byc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {

    LikeDislike findByPostAndUser(Post post, AppUser user);

    LikeDislike findByCommentAndUser(Comment comment, AppUser user);

    LikeDislike findByCommentReplyAndUser(CommentReply comment, AppUser user);
}

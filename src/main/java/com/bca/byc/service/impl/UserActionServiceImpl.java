package com.bca.byc.service.impl;

import com.bca.byc.entity.*;
import com.bca.byc.repository.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import static com.bca.byc.repository.handler.HandlerRepository.*;

@Service
@AllArgsConstructor
public class UserActionServiceImpl implements UserActionService {

    private final AppUserRepository userRepository;
    private final UserActionRepository userActionRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    @Override
    public void followUser(String userId, String email) {
        AppUser user = getUserByEmail(email, userRepository, "User not found in email: " + email);
        AppUser userToFollow = getEntityUserBySecureId(userId, userRepository, "User not found");

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userActionRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(String userId, String email) {
        AppUser user = getUserByEmail(email, userRepository, "User not found in email: " + email);
        AppUser userToUnfollow = getEntityUserBySecureId(userId, userRepository, "User not found");

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userActionRepository.save(user);
        }
    }

    @Override
    public void likeDislikePost(@PathVariable("commentId") String postId, String email,@PathVariable("isLike") Boolean isLike) {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found in email: " + email);

        LikeDislike existingLikeDislike = likeDislikeRepository.findByPostAndUser(post, user);

        if (existingLikeDislike != null) {
            if (existingLikeDislike.getIsLike() == isLike) {
                likeDislikeRepository.delete(existingLikeDislike);
            } else {
                existingLikeDislike.setIsLike(isLike);
                likeDislikeRepository.save(existingLikeDislike);
            }
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            newLikeDislike.setIsLike(isLike);
            newLikeDislike.setPost(post);
            newLikeDislike.setUser(user);
            likeDislikeRepository.save(newLikeDislike);
        }
    }

    @Override
    public void likeDislikeComment(@PathVariable("commentId") String commentId, String email,@PathVariable("isLike") Boolean isLike) {
        Comment comment = getEntityBySecureId(commentId, commentRepository, "Comment not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        LikeDislike existingLikeDislike = likeDislikeRepository.findByCommentAndUser(comment, user);

        if (existingLikeDislike != null) {
            if (existingLikeDislike.getIsLike() == isLike) {
                likeDislikeRepository.delete(existingLikeDislike);
            } else {
                existingLikeDislike.setIsLike(isLike);
                likeDislikeRepository.save(existingLikeDislike);
            }
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            newLikeDislike.setIsLike(isLike);
            newLikeDislike.setComment(comment);
            newLikeDislike.setUser(user);
            likeDislikeRepository.save(newLikeDislike);
        }
    }

    @Override
    public void likeDislikeCommentReply(@PathVariable("commentId") String commentId, String email,@PathVariable("isLike") Boolean isLike) {
        CommentReply comment = getEntityBySecureId(commentId, commentReplyRepository, "Comment not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        LikeDislike existingLikeDislike = likeDislikeRepository.findByCommentReplyAndUser(comment, user);

        if (existingLikeDislike != null) {
            if (existingLikeDislike.getIsLike() == isLike) {
                likeDislikeRepository.delete(existingLikeDislike);
            } else {
                existingLikeDislike.setIsLike(isLike);
                likeDislikeRepository.save(existingLikeDislike);
            }
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            newLikeDislike.setIsLike(isLike);
            newLikeDislike.setCommentReply(comment);
            newLikeDislike.setUser(user);
            likeDislikeRepository.save(newLikeDislike);
        }
    }
}

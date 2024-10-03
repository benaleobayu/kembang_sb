package com.bca.byc.service.impl;

import com.bca.byc.entity.*;
import com.bca.byc.model.attribute.SetLikeDislikeRequest;
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
    private final UserHasSavedPostRepository userHasSavedPostRepository;
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
    public void likeDislikePost(@PathVariable("commentId") String postId, String email, @PathVariable("isLike") SetLikeDislikeRequest isLike) {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found in email: " + email);

        handleLikeDislike(user, post, null, null, isLike);
    }

    @Override
    public void likeDislikeComment(@PathVariable("commentId") String commentId, String email, @PathVariable("isLike") SetLikeDislikeRequest isLike) {
        Comment comment = getEntityBySecureId(commentId, commentRepository, "Comment not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        handleLikeDislike(user, null, comment, null, isLike);
    }

    @Override
    public void likeDislikeCommentReply(@PathVariable("commentId") String commentId, String email, @PathVariable("isLike") SetLikeDislikeRequest isLike) {
        CommentReply comment = getEntityBySecureId(commentId, commentReplyRepository, "Comment not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        handleLikeDislike(user, null, null, comment, isLike);
    }

    @Override
    public String saveUnsavePost(String postId, String email) {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        UserHasSavedPost existingSavedPost = userHasSavedPostRepository.findByPostAndUser(post, user);
        if (existingSavedPost != null) {
            userHasSavedPostRepository.delete(existingSavedPost);
            return "success remove saved post";
        } else {
            UserHasSavedPost newSavedPost = new UserHasSavedPost();
            newSavedPost.setPost(post);
            newSavedPost.setUser(user);
            userHasSavedPostRepository.save(newSavedPost);
            return "success save post";
        }
    }


    // --------------------------------------------------------------------
    private void handleLikeDislike(AppUser user, Post post, Comment comment, CommentReply commentReply, SetLikeDislikeRequest isLike) {
        LikeDislike existingLikeDislike = null;

        if (post != null) {
            existingLikeDislike = likeDislikeRepository.findByPostAndUser(post, user);
        } else if (comment != null) {
            existingLikeDislike = likeDislikeRepository.findByCommentAndUser(comment, user);
        } else if (commentReply != null) {
            existingLikeDislike = likeDislikeRepository.findByCommentReplyAndUser(commentReply, user);
        }

        if (existingLikeDislike != null) {
            if (existingLikeDislike.getIsLike() == isLike.getIsLike()) {
                likeDislikeRepository.delete(existingLikeDislike);
            } else {
                existingLikeDislike.setIsLike(isLike.getIsLike());
                likeDislikeRepository.save(existingLikeDislike);
            }
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            newLikeDislike.setIsLike(isLike.getIsLike());
            if (post != null) {
                newLikeDislike.setPost(post);
            } else if (comment != null) {
                newLikeDislike.setComment(comment);
            } else if (commentReply != null) {
                newLikeDislike.setCommentReply(commentReply);
            }
            newLikeDislike.setUser(user);
            likeDislikeRepository.save(newLikeDislike);
        }
    }

}

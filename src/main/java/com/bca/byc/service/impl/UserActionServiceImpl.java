package com.bca.byc.service.impl;

import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.repository.*;
import com.bca.byc.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserActionServiceImpl implements UserActionService {

    private final UserActionRepository userActionRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    @Override
    public void followUser(Long userId, String email) {
        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToFollow = userActionRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userActionRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(Long userId, String email) {
        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToUnfollow = userActionRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userActionRepository.save(user);
        }
    }

    @Override
    public void likeDislikePost(Long postId, String email, Boolean isLike) {
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new ResourceNotFoundException("Post not found"));

        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));

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
    public void likeDislikeComment(Long id, String email, Boolean isLike) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Post not found"));

        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));

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
    public void likeDislikeCommentReply(Long id, String email, Boolean isLike) {
        CommentReply comment = commentReplyRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Post not found"));

        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));

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

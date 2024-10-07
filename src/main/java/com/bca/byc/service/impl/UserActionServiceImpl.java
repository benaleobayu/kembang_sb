package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.repository.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

        // if user = userToFollow return error
        if (user.getId().equals(userToFollow.getId())) {
            throw new BadRequestException("User cannot follow itself");
        }

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userActionRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(String userId, String email) {
        AppUser user = getUserByEmail(email, userRepository, "User not found in email: " + email);
        AppUser userToUnfollow = getEntityUserBySecureId(userId, userRepository, "User not found");

        // if user = userToUnfollow return error
        if (user.getId().equals(userToUnfollow.getId())) {
            throw new BadRequestException("User cannot unfollow itself");
        }

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userActionRepository.save(user);
        }
    }

    @Override
    public TotalCountResponse likeDislike(String email, SetLikeDislikeRequest dto) {
        AppUser user = GlobalConverter.getUserEntity(userRepository);
        LikeDislike existingLikeDislike = null;

        if ("POST".equals(dto.getType())) {
            Post post = postRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByPostAndUser(post, user);
        } else if ("COMMENT".equals(dto.getType())) {
            Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByCommentAndUser(comment, user);
        } else if ("COMMENT_REPLY".equals(dto.getType())) {
            CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByCommentReplyAndUser(commentReply, user);
        }

        // Logic toggle
        if (existingLikeDislike != null) {
            likeDislikeRepository.delete(existingLikeDislike);
            // decrease like count
            if ("POST".equals(dto.getType())) {
                Post post = postRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
                post.setLikesCount(post.getLikesCount() - 1);
                postRepository.save(post);
            } else if ("COMMENT".equals(dto.getType())) {
                Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
                comment.setLikesCount(comment.getLikesCount() - 1);
                commentRepository.save(comment);
            } else if ("COMMENT_REPLY".equals(dto.getType())) {
                CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
                commentReply.setLikesCount(commentReply.getLikesCount() - 1);
                commentReplyRepository.save(commentReply);
            }
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            TotalCountResponse message = new TotalCountResponse();
            if ("POST".equals(dto.getType())) {
                Post post = postRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
                newLikeDislike.setPost(post);
                newLikeDislike.setUser(user);
                likeDislikeRepository.save(newLikeDislike);
                post.setLikesCount(post.getLikesCount() + 1);
                postRepository.save(post);
                message.setTotal(likeDislikeRepository.countByPostId(post.getId()));
            } else if ("COMMENT".equals(dto.getType())) {
                Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
                newLikeDislike.setComment(comment);
                newLikeDislike.setUser(user);
                likeDislikeRepository.save(newLikeDislike);
                comment.setLikesCount(comment.getLikesCount() + 1);
                commentRepository.save(comment);
                message.setTotal(likeDislikeRepository.countByCommentId(comment.getId()));
            } else if ("COMMENT_REPLY".equals(dto.getType())) {
                CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
                newLikeDislike.setCommentReply(commentReply);
                newLikeDislike.setUser(user);
                likeDislikeRepository.save(newLikeDislike);
                commentReply.setLikesCount(commentReply.getLikesCount() + 1);
                commentReplyRepository.save(commentReply);
                message.setTotal(likeDislikeRepository.countByCommentReplyId(commentReply.getId()));
            }
            return message;
        }

        return null;
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

}

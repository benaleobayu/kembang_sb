package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeNotification;
import com.bca.byc.entity.*;
import com.bca.byc.entity.auth.PostShared;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PostShareResponse;
import com.bca.byc.model.apps.NotificationCreateRequest;
import com.bca.byc.model.attribute.SetLikeDislikeRequest;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.model.returns.ReturnIsSavedResponse;
import com.bca.byc.repository.*;
import com.bca.byc.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bca.byc.converter.parsing.TreeNotification.saveNotification;
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
    private final PostSharedRepository postSharedRepository;
    private final NotificationRepository notificationRepository;

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
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        LikeDislike existingLikeDislike = null;
        TotalCountResponse response = new TotalCountResponse();
        TreeNotification newNotification = new TreeNotification();

        if ("POST".equals(dto.getType())) {
            Post post = postRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByPostAndUser(post, userLogin);
        } else if ("COMMENT".equals(dto.getType())) {
            Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByCommentAndUser(comment, userLogin);
        } else if ("COMMENT_REPLY".equals(dto.getType())) {
            CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                    .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
            existingLikeDislike = likeDislikeRepository.findByCommentReplyAndUser(commentReply, userLogin);
        }

        // Logic toggle
        if (existingLikeDislike != null) {
            likeDislikeRepository.delete(existingLikeDislike);
            // decrease like count
            if ("POST".equals(dto.getType())) {
                Post post = postRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
                post.setLikesCount(post.getLikesCount() - 1);
                Post savedData = postRepository.save(post);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
            } else if ("COMMENT".equals(dto.getType())) {
                Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
                comment.setLikesCount(comment.getLikesCount() - 1);
                Comment savedData = commentRepository.save(comment);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
            } else if ("COMMENT_REPLY".equals(dto.getType())) {
                CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
                commentReply.setLikesCount(commentReply.getLikesCount() - 1);
                CommentReply savedData = commentReplyRepository.save(commentReply);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
            }
            response.setIsLiked(false);
        } else {
            LikeDislike newLikeDislike = new LikeDislike();
            NotificationCreateRequest request = null;
            if ("POST".equals(dto.getType())) {
                Post post = postRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Post tidak ditemukan"));
                newLikeDislike.setPost(post);
                newLikeDislike.setUser(userLogin);
                likeDislikeRepository.save(newLikeDislike);
                post.setLikesCount(post.getLikesCount() + 1);
                Post savedData = postRepository.save(post);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
                request = new NotificationCreateRequest(
                        "USER", "LIKE-POST", savedData.getSecureId(), "LIKE", userLogin.getId(), post.getUser()
                );
            } else if ("COMMENT".equals(dto.getType())) {
                Comment comment = commentRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Komentar tidak ditemukan"));
                newLikeDislike.setComment(comment);
                newLikeDislike.setUser(userLogin);
                likeDislikeRepository.save(newLikeDislike);
                comment.setLikesCount(comment.getLikesCount() + 1);
                Comment savedData = commentRepository.save(comment);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
                request = new NotificationCreateRequest(
                        "USER", "LIKE-COMMENT", savedData.getSecureId(), "LIKE", userLogin.getId(), comment.getUser()
                );
            } else if ("COMMENT_REPLY".equals(dto.getType())) {
                CommentReply commentReply = commentReplyRepository.findBySecureId(dto.getTargetId())
                        .orElseThrow(() -> new BadRequestException("Balasan komentar tidak ditemukan"));
                newLikeDislike.setCommentReply(commentReply);
                newLikeDislike.setUser(userLogin);
                likeDislikeRepository.save(newLikeDislike);
                commentReply.setLikesCount(commentReply.getLikesCount() + 1);
                CommentReply savedData = commentReplyRepository.save(commentReply);
                response.setTotal(Math.toIntExact(savedData.getLikesCount()));
                request = new NotificationCreateRequest(
                        "USER", "LIKE-COMMENT-REPLY", savedData.getSecureId(), "LIKE", userLogin.getId(), commentReply.getUser()
                );
            }
            assert request != null;
            saveNotification(request, notificationRepository);
            response.setIsLiked(true);
            return response;
        }

        return response;
    }


    @Override
    public ReturnIsSavedResponse saveUnsavePost(String postId) {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = GlobalConverter.getUserEntity(userRepository);

        ReturnIsSavedResponse message = new ReturnIsSavedResponse();

        UserHasSavedPost existingSavedPost = userHasSavedPostRepository.findByPostAndUser(post, user);
        if (existingSavedPost != null) {
            userHasSavedPostRepository.delete(existingSavedPost);
            message.setMessage("Post unsave successfully");
            message.setIsSaved(false);
            return message;
        } else {
            UserHasSavedPost newSavedPost = new UserHasSavedPost();
            newSavedPost.setPost(post);
            newSavedPost.setUser(user);
            userHasSavedPostRepository.save(newSavedPost);
            message.setMessage("Post saved successfully");
            message.setIsSaved(true);
            return message;
        }
    }

    @Override
    public String sharePost(PostShareResponse dto, String email) {
        Post post = getEntityBySecureId(dto.getPostId(), postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");

        if (dto.getSharedUserIds() == null || dto.getSharedUserIds().isEmpty()) {
            throw new IllegalArgumentException("No user IDs provided to share the post.");
        }
        if (dto.getSharedUserIds().size() > 15) {
            throw new IllegalArgumentException("You can only share the post with a maximum of 15 users.");
        }

        // Share the post with each user in the list
        for (String sharedUserId : dto.getSharedUserIds()) {
            AppUser sharedUser = getEntityUserBySecureId(sharedUserId, userRepository, "Shared user not found");
            PostShared postShared = new PostShared();
            postShared.setPost(post);
            postShared.setFromUser(user);
            postShared.setToUser(sharedUser); // Set the shared user

            postSharedRepository.save(postShared);
        }

        return "Post shared successfully";
    }

}

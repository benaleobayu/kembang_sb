package com.bca.byc.service.impl;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.CommentServiceReply;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class CommentServiceReplyImpl implements CommentServiceReply {

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

    @Override
    public void saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String parentCommentId) {
        String secureId = ContextPrincipal.getSecureUserId();
        AppUser user = userRepository.findBySecureId(secureId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        Comment comment = commentRepository.findBySecureId(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BadRequestException("Comment not found");
        }
        // set entity to add with model mapper
        CommentReply data = new CommentReply();
        data.setComment(dto.getComment());
        data.setParentComment(comment);
        data.setUser(user);
        // save data
        commentReplyRepository.save(data);
    }

    @Override
    public void updateDataCommentReply(String postId, String parentCommentId, CommentCreateUpdateRequest dto, String id) {
        String secureId = ContextPrincipal.getSecureUserId();
        AppUser user = userRepository.findBySecureId(secureId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        Comment comment = commentRepository.findBySecureId(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        CommentReply data = commentReplyRepository.findBySecureId(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BadRequestException("Comment does not belong to the specified post");
        }
        if (!data.getParentComment().getId().equals(comment.getId())) {
            throw new BadRequestException("Reply does not belong to the specified comment");
        }
        if (!data.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Comment does not belong to the specified user");
        }
        // set entity to add with model mapper
        data.setComment(dto.getComment());
        // save data
        commentReplyRepository.save(data);
    }

    @Override
    public void deleteDataCommentReply(String postId, String parentCommentId, String id) {
        String secureId = ContextPrincipal.getSecureUserId();
        AppUser user = userRepository.findBySecureId(secureId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        Comment comment = commentRepository.findBySecureId(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        CommentReply data = commentReplyRepository.findBySecureId(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BadRequestException("Comment does not belong to the specified post");
        }
        if (!data.getParentComment().getId().equals(comment.getId())) {
            throw new BadRequestException("Reply does not belong to the specified comment");
        }
        if (!data.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Comment does not belong to the specified user");
        }

        commentReplyRepository.delete(data);
    }
}

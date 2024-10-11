package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.CommentServiceReply;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class CommentServiceReplyImpl implements CommentServiceReply {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

    @Override
    public ResultPageResponseDTO<ListCommentReplyResponse> listDataCommentReplies(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId, String parentCommentId) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Comment comment = HandlerRepository.getIdBySecureId(
                parentCommentId,
                commentRepository::findBySecureId,
                projection -> commentRepository.findById(projection.getId()),
                "Comment not found"
        );
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<CommentReply> pageResult = commentReplyRepository.findListDataCommentUser(comment.getId(), pageable);
        List<ListCommentReplyResponse> dtos = pageResult.stream().map((c) -> {
            TreePostConverter dataConverter = new TreePostConverter(baseUrl);
            ListCommentReplyResponse dto = new ListCommentReplyResponse();

            dataConverter.convertToListCommentReplyResponse(
                    dto,
                    c.getSecureId(),
                    c.getId(),
                    c.getComment(),
                    c.getUser(),
                    c.getCreatedAt()
            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public TotalCountResponse saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String parentCommentId) {
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

        // incrase comment post
        TreePostConverter postConverter = new TreePostConverter(null);
        postConverter.countPostComments(post, postRepository, "add");
        data.setCommentsCount(data.getCommentsCount() + 1);

        // save data
        CommentReply savedReply = commentReplyRepository.save(data);

        int totalComments = savedReply.getParentComment().getPost().getCommentsCount().intValue();

        TotalCountResponse message = new TotalCountResponse();
        message.setTotal(totalComments);
        return message;
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

        // decrase comment post
        TreePostConverter postConverter = new TreePostConverter(null);
        postConverter.countPostComments(post, postRepository, "delete");

        commentReplyRepository.delete(data);
    }
}

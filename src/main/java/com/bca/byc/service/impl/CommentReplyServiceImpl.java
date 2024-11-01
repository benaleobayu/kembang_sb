package com.bca.byc.service.impl;

import com.bca.byc.converter.CommentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.MaskingBlacklistKeyword;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.apps.NotificationCreateRequest;
import com.bca.byc.model.returns.ReturnCommentResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.*;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.CommentReplyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeNotification.saveNotification;
import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class CommentReplyServiceImpl implements CommentReplyService {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

    private final LikeDislikeRepository likeDislikeRepository;
    private final BlacklistKeywordRepository blacklistKeywordRepository;

    private final NotificationRepository notificationRepository;

    private CommentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ListCommentReplyResponse> listDataCommentReplies(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId, String parentCommentId) {
        AppUser user = GlobalConverter.getUserEntity(userRepository);
        Comment comment = HandlerRepository.getIdBySecureId(
                parentCommentId,
                commentRepository::findBySecureId,
                projection -> commentRepository.findById(projection.getId()),
                "Comment not found"
        );
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<CommentReply> pageResult = commentReplyRepository.findListDataCommentUser(comment.getId(), set.pageable());
        List<ListCommentReplyResponse> dtos = pageResult.stream().map((c) -> {
            TreePostConverter dataConverter = new TreePostConverter(baseUrl);
            ListCommentReplyResponse dto = new ListCommentReplyResponse();

            dataConverter.convertToListCommentReplyResponse(
                    dto,
                    c,
                    user,
                    likeDislikeRepository

            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public ReturnCommentResponse saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String parentCommentId) {
        String secureId = ContextPrincipal.getSecureUserId();
        AppUser user = userRepository.findBySecureId(secureId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        Comment comment = commentRepository.findBySecureId(parentCommentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BadRequestException("Comment not found");
        }
        // set entity to add with model mapper
        CommentReply data = new CommentReply();
        String sanitizedWord = MaskingBlacklistKeyword.sanitizeWord(dto.getComment(), blacklistKeywordRepository);
        data.setComment(sanitizedWord);
        data.setParentComment(comment);
        data.setUser(user);

        // incrase comment post
        TreePostConverter postConverter = new TreePostConverter(null);
        postConverter.countPostComments(post, postRepository, "add");
        data.setCommentsCount(data.getCommentsCount() + 1);

        // save data
        CommentReply savedReply = commentReplyRepository.save(data);

        NotificationCreateRequest newNotification = new NotificationCreateRequest(
                "USER", "COMMENT-REPLY", savedReply.getSecureId(), savedReply.getComment(), savedReply.getUser().getId(), post.getUser()
        );
        saveNotification(newNotification, notificationRepository);

        int totalComments = savedReply.getParentComment().getPost().getStats().getCommentsCount().intValue();
        int totalReplies = savedReply.getParentComment().getCommentReply().size();

        ReturnCommentResponse message = new ReturnCommentResponse();
        message.setCommentDetail(converter.convertToListRepliesResponse(savedReply));
        message.setTotalComments(totalComments);
        message.setTotalReplies(totalReplies);
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

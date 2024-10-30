package com.bca.byc.service.impl;

import com.bca.byc.converter.CommentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.apps.NotificationCreateRequest;
import com.bca.byc.model.returns.ReturnCommentResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.*;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeNotification.saveNotification;
import static com.bca.byc.exception.MessageExceptionHandler.checkCommentOnPost;
import static com.bca.byc.exception.MessageExceptionHandler.checkCommentUser;
import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;
import static com.bca.byc.repository.handler.HandlerRepository.getUserByEmail;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final AppUserRepository appUserRepository;

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final PostRepository postRepository;
    private final AppUserRepository userRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final NotificationRepository notificationRepository;

    private CommentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ListCommentResponse> listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);

        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Comment> pageResult = commentRepository.findListDataComment(postId, set.pageable());
        List<ListCommentResponse> dtos = pageResult.stream().map((c) -> {
            TreePostConverter dataConverter = new TreePostConverter(baseUrl);
            ListCommentResponse dto = new ListCommentResponse();

            dataConverter.convertToListCommentResponse(
                    dto,
                    user,
                    c,
                    likeDislikeRepository
            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public CommentDetailResponse findDataById(String postId, String commentId) throws BadRequestException {
        Comment data = getEntityBySecureId(commentId, commentRepository, "Comment not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public ReturnCommentResponse saveData(String postId, @Valid CommentCreateUpdateRequest dto, String email) throws BadRequestException {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");

        // set entity to add with model mapper
        Comment data = converter.convertToCreateRequest(post, dto, email);
        // add count comment
        TreePostConverter postConverter = new TreePostConverter(baseUrl);
        postConverter.countPostComments(post, postRepository, "add");
        data.setCommentsCount(data.getCommentsCount() + 1);

        // save data
        Comment savedComment = commentRepository.save(data);

        NotificationCreateRequest newNotification = new NotificationCreateRequest(
                "USER", "COMMENT", savedComment.getSecureId(), savedComment.getComment(), savedComment.getUser().getId(), post.getUser()
                );
        saveNotification(newNotification, notificationRepository);

        int totalComments = savedComment.getPost().getCommentsCount().intValue();
        int totalReplies = savedComment.getCommentReply().size();

        ReturnCommentResponse message = new ReturnCommentResponse();
        message.setCommentDetail(converter.convertToListResponse(savedComment));
        message.setTotalComments(totalComments);
        message.setTotalReplies(totalReplies);
        return message;
    }

    @Override
    public void updateData(String postId, String commentId, CommentCreateUpdateRequest dto, String email) throws BadRequestException {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");
        Comment comment = getEntityBySecureId(commentId, commentRepository, "Comment not found");
        // check data
        checkCommentOnPost(comment, post, "update");
        checkCommentUser(comment, user, "update");

        // update
        converter.convertToUpdateRequest(comment, dto);

        // update the updated_at
        comment.setUpdatedAt(LocalDateTime.now());

        // save
        commentRepository.save(comment);
    }

    @Override
    public void deleteData(String postId, String commentId, String email) throws ResourceNotFoundException {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");
        AppUser user = getUserByEmail(email, userRepository, "User not found");
        Comment comment = getEntityBySecureId(commentId, commentRepository, "Comment not found");
        // check data
        checkCommentOnPost(comment, post, "delete");
        checkCommentUser(comment, user, "delete");

        // decrease count comment
        TreePostConverter postConverter = new TreePostConverter(baseUrl);
        postConverter.countPostComments(post, postRepository, "delete");

        // delete data
        commentRepository.deleteById(comment.getId());
    }

}

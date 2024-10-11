package com.bca.byc.service.impl;

import com.bca.byc.converter.CommentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.*;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.CommentService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.exception.MessageExceptionHandler.checkCommentOnPost;
import static com.bca.byc.exception.MessageExceptionHandler.checkCommentUser;
import static com.bca.byc.repository.handler.HandlerRepository.*;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    @Value("${app.base.url}")
    private final String baseUrl;

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final PostRepository postRepository;
    private final AppUserRepository userRepository;

    private CommentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ListCommentResponse> listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId) {
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
                    c.getSecureId(),
                    c.getId(),
                    c.getComment(),
                    c.getCommentReply(),
                    c.getUser(),
                    c.getCreatedAt()
            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public CommentDetailResponse findDataById(String postId, String commentId) throws BadRequestException {
        Comment data = getEntityBySecureId(commentId, commentRepository, "Comment not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public TotalCountResponse saveData(String postId, @Valid CommentCreateUpdateRequest dto, String email) throws BadRequestException {
        Post post = getEntityBySecureId(postId, postRepository, "Post not found");

        // set entity to add with model mapper
        Comment data = converter.convertToCreateRequest(post, dto, email);
        // add count comment
        TreePostConverter postConverter = new TreePostConverter(baseUrl);
        postConverter.countPostComments(post, postRepository, "add");
        data.setCommentsCount(data.getCommentsCount() + 1);

        // save data
        Comment savedComment = commentRepository.save(data);

        int totalComments = savedComment.getPost().getCommentsCount().intValue();

        TotalCountResponse message = new TotalCountResponse();
        message.setTotal(totalComments);
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

package com.bca.byc.service.impl;

import com.bca.byc.converter.CommentDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.CommentService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.exception.MessageExceptionHandler.checkCommentOwnership;
import static com.bca.byc.exception.MessageExceptionHandler.checkCommentUser;
import static com.bca.byc.repository.handler.HandlerRepository.*;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private AppUserRepository userRepository;

    private CommentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ListCommentResponse> listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Comment> pageResult = commentRepository.findListDataComment(postId, keyword, pageable);
        List<ListCommentResponse> dtos = pageResult.stream().map((c) -> {
            ListCommentResponse dto = converter.convertToPageListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public CommentDetailResponse findDataById(String postId, Long id) throws BadRequestException {
        Comment data = commentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(String postId, @Valid CommentCreateRequest dto, String email) throws BadRequestException {
        Post post = postRepository.findBySecureId(postId)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        // set entity to add with model mapper
        Comment data = converter.convertToCreateRequest(post, dto, email);
        // save data
        commentRepository.save(data);
    }

    @Override
    public void updateData(String postId, Long id, CommentCreateRequest dto, String email) throws BadRequestException {
        Post post = getEntityPostBySecureId(postId, postRepository, "Post not found");
        AppUser user = getEntityByEmail(email, userRepository, "User not found");
        Comment comment = getEntityById(id, commentRepository, "Comment not found");
        // check data
        checkCommentOwnership(comment, post);
        checkCommentUser(comment, user);

        // update
        converter.convertToUpdateRequest(comment, dto);

        // update the updated_at
        comment.setUpdatedAt(LocalDateTime.now());

        // save
        commentRepository.save(comment);
    }

    @Override
    public void deleteData(String postId, Long id, String email) throws ResourceNotFoundException {
        Post post = getEntityPostBySecureId(postId, postRepository, "Post not found");
        AppUser user = getEntityByEmail(email, userRepository, "User not found");
        Comment comment = getEntityById(id, commentRepository, "Comment not found");
        // check data
        checkCommentOwnership(comment, post);
        checkCommentUser(comment, user);

        // delete data
        commentRepository.deleteById(id);
    }

}

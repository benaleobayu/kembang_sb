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

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private AppUserRepository userRepository;

    private CommentDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ListCommentResponse> listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, Long postId) {
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
    public CommentDetailResponse findDataById(Long postId, Long id) throws BadRequestException {
        Comment data = commentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(Long postId, @Valid CommentCreateRequest dto) throws BadRequestException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        // set entity to add with model mapper
        Comment data = converter.convertToCreateRequest(post, dto);
        // save data
        commentRepository.save(data);
    }

    @Override
    public void updateData(Long postId, Long id, CommentCreateRequest dto) throws BadRequestException {
        // check exist and get
        Comment data = commentRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Comment ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        commentRepository.save(data);
    }

    @Override
    public void deleteData(Long postId, Long id, String email) throws ResourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // jika comment bukan milik post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Comment bukan milik post");
        }

        // jika comment bukan milik user
        if (!comment.getPost().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Comment bukan milik user");
        }

        // delete data
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment tidak ada");
        } else {
            commentRepository.deleteById(id);
        }
    }
}

package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.repository.PostContentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PostService;
import com.bca.byc.util.PaginationUtil;
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
public class PostServiceImpl implements PostService {

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final PostDTOConverter converter;
    private final PostContentRepository postContentRepository;

    @Override
    public void save(String email, PostCreateUpdateRequest dto, List<PostContent> contentList) throws Exception, InvalidFileTypeException {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Post post = converter.convertToCreateRequest(user, dto);

        Post savedPost = postRepository.save(post);

        for (PostContent postContent : contentList) {
            postContent.setPost(savedPost);
            postContentRepository.save(postContent);
        }
    }

    @Override
    public PostDetailResponse findById(Long id) throws Exception {
        Post data = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        return converter.convertToDetailResponse(data);
    }

    @Override
    public void update(Long id, PostCreateUpdateRequest post) throws Exception {

        Post data = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        converter.convertToUpdateRequest(data, post);

        data.setUpdatedAt(LocalDateTime.now());

        postRepository.save(data);
    }

    @Override
    public void deleteData(Long id) throws Exception {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        } else {
            postRepository.deleteById(id);
        }
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listData(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Long userId = user.getId();

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Post> pageResult = postRepository.findRandomPosts(keyword, pageable);

        assert pageResult != null;
        List<PostHomeResponse> dtos = pageResult.stream().map((c) -> {
            PostHomeResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }

}

package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.repository.PostContentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostDTOConverter converter;

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;


    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataPostHome(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword, String category) {
        // Get user id
        AppUser user = HandlerRepository.getUserByEmail(email, appUserRepository, "User not found");
        Long userId = user.getId();

        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        Page<Post> pageResult = null;
        if (Objects.equals(category, "top-picks")) {
            pageResult = postRepository.findRandomPosts(keyword, pageable);
        }
        if (Objects.equals(category, "following")) {
            pageResult = postRepository.findPostByFollowingUsers(userId, keyword, pageable);
        }
        if (Objects.equals(category, "discovery")) {
            pageResult = postRepository.findPostByOfficialUsers(keyword, pageable);
        }

        assert pageResult != null;
        List<PostHomeResponse> dtos = pageResult.stream().map((post) -> {
            PostHomeResponse dto = converter.convertToListResponse(post, userId);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }


    @Override
    public void save(String email, PostCreateUpdateRequest dto, List<PostContent> contentList) throws Exception, InvalidFileTypeException {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Post data = converter.convertToCreateRequest(user, dto);
        Post savedPost = postRepository.save(data);

        for (PostContent postContent : contentList) {
            postContent.setPost(savedPost);
            postContentRepository.save(postContent);
        }
    }

    @Override
    public PostHomeResponse findBySecureId(String secureId) {
        Post data = getEntityBySecureId(secureId, postRepository, "Post not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public void update(String secureId, PostCreateUpdateRequest post) throws Exception {

        Post data = postRepository.findBySecureId(secureId)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        converter.convertToUpdateRequest(data, post);

        data.setUpdatedAt(LocalDateTime.now());

        postRepository.save(data);
    }

    @Override
    public void deleteData(String secureId) throws Exception {
        if (!postRepository.existsBySecureId(secureId)) {
            throw new ResourceNotFoundException("Post not found");
        } else {
            postRepository.deleteBySecureId(secureId);
        }
    }

}

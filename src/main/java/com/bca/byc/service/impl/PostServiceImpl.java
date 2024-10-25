package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.PostContentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.PostService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
    public ResultPageResponseDTO<PostHomeResponse> listDataPostHome(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword, String category, Boolean isElastic) {
        // Get userLogin
        AppUser userLogin;
        if (email != null) {
            userLogin = HandlerRepository.getUserByEmail(email, appUserRepository, "User not found");
        } else {
            userLogin = null;
        }

        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Post> pageResult = null;
        if (Objects.equals(category, "top-picks")) {
            pageResult = postRepository.findRandomPosts(set.keyword(), set.pageable());
        }
        if (Objects.equals(category, "following")) {
            if (userLogin == null) {
                throw new ResourceNotFoundException("User not found");
            }
            pageResult = postRepository.findPostByFollowingUsers(userLogin.getId(), set.keyword(), set.pageable());
        }
        if (Objects.equals(category, "discover")) {
            if (userLogin == null) {
                throw new ResourceNotFoundException("User not found");
            }
            pageResult = postRepository.findPostByOfficialUsers(set.keyword(), set.pageable());
        }

        assert pageResult != null;
        List<PostHomeResponse> dtos = pageResult.stream().map((post) -> {
            return converter.convertToDetailResponse(post, userLogin);
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public void save(String email, PostCreateUpdateRequest dto, List<PostContent> contentList) throws Exception, InvalidFileTypeImageException {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        Post data = converter.convertToCreateRequest(user, dto);

        TreePostConverter treePostConverter = new TreePostConverter(null);
        String contentType = treePostConverter.getContentTypePost(contentList);

        data.setContentType(contentType);
        Post savedPost = postRepository.save(data);

        for (PostContent postContent : contentList) {
            postContent.setPost(savedPost);
            postContentRepository.save(postContent);
        }
    }

    @Override
    public PostHomeResponse findBySecureId(String secureId) {
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);
        Post data = getEntityBySecureId(secureId, postRepository, "Post not found");

        return converter.convertToDetailResponse(data, user);
    }

    @Override
    public void update(String secureId, PostCreateUpdateRequest post) throws Exception {

        Post data = HandlerRepository.getIdBySecureId(
                secureId,
                postRepository::findBySecureId,
                projection -> postRepository.findById(projection.getId()),
                "Post not found"
        );

        converter.convertToUpdateRequest(data, post);

        data.setUpdatedAt(LocalDateTime.now());

        postRepository.save(data);
    }

    @Override
    @Transactional
    public void deleteData(String secureId) throws Exception {
        Post data = HandlerRepository.getIdBySecureId(
                secureId,
                postRepository::findBySecureId,
                projection -> postRepository.findById(projection.getId()),
                "Post not found"
        );

        if (!postRepository.existsById(data.getId())) {
            throw new ResourceNotFoundException("Post not found");
        } else {
            postRepository.deletePostById(data.getId());
        }
    }

}

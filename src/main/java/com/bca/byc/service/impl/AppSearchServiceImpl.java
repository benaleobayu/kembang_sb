package com.bca.byc.service.impl;

import com.bca.byc.converter.AppSearchDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.converter.parsing.TreeUserResponse;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.SuggestedUserResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.model.search.SearchDTOResponse;
import com.bca.byc.repository.AppSearchRepository;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppSearchService;
import com.bca.byc.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppSearchServiceImpl implements AppSearchService {

    private final AppUserRepository appUserRepository;
    private final AppSearchRepository repository;
    private final PostRepository postRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final LikeDislikeRepository likeDislikeRepository;

    private final AppSearchDTOConverter converter;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listResultPosts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(appUserRepository);

        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Post> pageResult = postRepository.findPostByLobAndDescription(set.keyword(), set.pageable());

        assert pageResult != null;
        List<PostHomeResponse> dtos = pageResult.stream().map((data) -> {
            TreePostConverter treePostConverter = new TreePostConverter(baseUrl, appUserRepository);
            PostHomeResponse dto = treePostConverter.convertToPostHomeResponse(
                    new PostHomeResponse(),
                    data,
                    treePostConverter,
                    userLogin,
                    likeDislikeRepository
            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AppSearchDetailResponse> listResultTags(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        Long userId = user.getId();

        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Post> pageResult = postRepository.findRandomPosts(set.keyword(), set.pageable());

        assert pageResult != null;
        List<AppSearchDetailResponse> dtos = pageResult.stream().map((c) -> {
            AppSearchDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<SuggestedUserResponse> listResultAccounts(String email, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = HandlerRepository.getUserByEmail(email, appUserRepository, "User not found");
        Long userId = user.getId();
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<AppUser> pageResult = appUserRepository.findUserByNameAndLob(userId, keyword, set.pageable());

        assert pageResult != null;
        List<SuggestedUserResponse> dtos = pageResult.getContent().stream()
                .map(data -> TreeUserResponse.convertToCardUser(data, baseUrl)) // Convert AppUser to SuggestedUserResponse
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<SuggestedUserResponse> listSuggestedUser(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<AppUser> pageResult = appUserRepository.findRandomUsers(set.keyword(), set.pageable());

        List<SuggestedUserResponse> dtos = pageResult.getContent().stream()
                .map(user -> TreeUserResponse.convertToCardUser(user, baseUrl)) // Convert AppUser to SuggestedUserResponse
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<SearchDTOResponse> listSearch(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        Page<SearchDTOResponse> pageResult = businessCategoryRepository.findBusinessByKeyword(keyword, pageable);

        assert pageResult != null;
        List<SearchDTOResponse> dtos = pageResult.stream().map((c) -> {
            SearchDTOResponse dto = new SearchDTOResponse();
            dto.setName(c.getName());
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

}

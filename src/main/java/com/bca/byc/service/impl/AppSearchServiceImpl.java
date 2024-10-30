package com.bca.byc.service.impl;

import com.bca.byc.converter.AppSearchDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.converter.parsing.TreeUserResponse;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppSearchDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.SuggestedUserResponse;
import com.bca.byc.model.projection.SuggestedUserProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.model.search.SearchDTOResponse;
import com.bca.byc.repository.*;
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
    private final AppSearchRepository appSearchRepository;
    private final BusinessRepository businessRepository;
    private final PostRepository postRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final BusinessHasCategoryRepository businessHasCategoryRepository;

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
            TreePostConverter treePostConverter = new TreePostConverter(baseUrl);
            PostHomeResponse dto = treePostConverter.convertToPostHomeResponse(
                    new PostHomeResponse(),
                    data,
                    treePostConverter,
                    userLogin,
                    likeDislikeRepository
            );
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
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

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<SuggestedUserResponse> listResultAccounts(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // get user id
        AppUser user = GlobalConverter.getUserEntity(appUserRepository);
        Long userId = user.getId();
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<SuggestedUserProjection> pageResult = appUserRepository.findUserByNameAndLob(userId, set.keyword(), set.pageable());

        assert pageResult != null;
        List<SuggestedUserResponse> dtos = pageResult.getContent().stream()
                .map(data -> {
                    SuggestedUserResponse response = new SuggestedUserResponse();
                    response.setUserId(data.getUserId());
                    response.setUserAvatar(GlobalConverter.getParseImage(data.getUserAvatar(), baseUrl));
                    response.setUserName(data.getUserName());
                    List<Business> businesses = businessRepository.findBusinessByUserId(data.getId());
                    Business firstBusiness = businesses.stream().filter(Business::getIsPrimary).findFirst().orElse(null);
                    assert firstBusiness != null;
                    response.setBusinessName(firstBusiness == null ? null : firstBusiness.getName());
                    response.setBusinessLob(firstBusiness == null ? null : firstBusiness.getBusinessCategories().stream()
                            .map(b -> b.getBusinessCategoryParent().getName())
                            .findFirst().orElse(null));
                    List<AppUser> followers = appUserRepository.findFollowersByUserId(data.getId());
                    boolean isFollowed = followers.stream().anyMatch(f -> f.getId().equals(user.getId()));
                    response.setIsFollowed(isFollowed);
                    return response;
                })
                .collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
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

        return PageCreateReturnApps.create(pageResult, dtos);
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

        return PageCreateReturnApps.create(pageResult, dtos);
    }

}

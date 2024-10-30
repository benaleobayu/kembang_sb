package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.UserHasSavedPostRepository;
import com.bca.byc.repository.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;
import com.bca.byc.util.FileUploadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserProfileServiceImpl implements AppUserProfileService {

    private final AppUserService appUserService;

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final UserHasSavedPostRepository userHasSavedPostRepository;
    private final CommentRepository commentRepository;

    private final PostDTOConverter postConverter;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public UserInfoResponse updateUserAvatar(MultipartFile avatar) throws IOException, InvalidFileTypeImageException {
        FileUploadHelper.validateFileTypeImage(avatar);
        AppUser appUser = GlobalConverter.getUserEntity(userRepository);

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldAvatar = userDetail.getAvatar();
        String avatarPath = FileUploadHelper.saveFile(avatar, UPLOAD_DIR + "/avatar");

        userDetail.setAvatar(avatarPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        AppUser savedUser = userRepository.save(appUser);

        if (oldAvatar != null && !oldAvatar.isEmpty()) {
            FileUploadHelper.deleteFile(oldAvatar, UPLOAD_DIR);
        }
        return appUserService.getUserDetailFromId(savedUser.getId());
    }

    @Override
    public UserInfoResponse updateUserCover(MultipartFile cover) throws IOException, InvalidFileTypeImageException {
        FileUploadHelper.validateFileTypeImage(cover);
        AppUser appUser = GlobalConverter.getUserEntity(userRepository);

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldCover = userDetail.getCover();
        String coverPath = FileUploadHelper.saveFile(cover, UPLOAD_DIR + "/cover");

        userDetail.setCover(coverPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        AppUser savedUser = userRepository.save(appUser);

        if (oldCover != null && !oldCover.isEmpty()) {
            FileUploadHelper.deleteFile(oldCover, UPLOAD_DIR);
        }
        return appUserService.getUserDetailFromId(savedUser.getId());
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataUserFollowAndFollowing(Integer pages, Integer limit, String sortBy, String direction, String keyword, String type, String userId) {
        AppUser userAccess = GlobalConverter.getUserEntity(userRepository);
        AppUser userVisit;
        if (userId == null) {
            userVisit = GlobalConverter.getUserEntity(userRepository);
        } else {
            userVisit = HandlerRepository.getEntityBySecureId(userId, userRepository, "user not found");
        }

        // Validate parameters
        if (pages < 0 || limit <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters.");
        }

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // Retrieve followers or following
        Page<AppUser> pageResult;
        if (type.equals("FOLLOWING")) {
            pageResult = userRepository.findFollowing(userVisit.getId(), userAccess.getId(), set.keyword(), set.pageable());
        } else if (type.equals("FOLLOWERS")) {
            pageResult = userRepository.findFollowers(userVisit.getId(), userAccess.getId(), set.keyword(), set.pageable());
        } else {
            throw new IllegalArgumentException("Invalid type. Must be 'FOLLOWING' or 'FOLLOWERS'.");
        }

        // Map results to DTOs
        List<PostOwnerResponse> dtos = pageResult.stream()
                .map(data -> {
                    TreePostConverter dataConverter = new TreePostConverter(baseUrl);

                    // Get the first primary business
                    Business firstBusiness = data.getBusinesses() == null ? null :
                            data.getBusinesses().stream().filter(Business::getIsPrimary).findFirst().orElse(null);

                    // Get the first category from the first business, if it exists
                    BusinessCategory firstBusinessCategory = null;
                    if (firstBusiness != null) {
                        firstBusinessCategory = firstBusiness.getBusinessCategories() == null ? null :
                                firstBusiness.getBusinessCategories().stream()
                                        .findFirst()
                                        .map(BusinessHasCategory::getBusinessCategoryParent)
                                        .orElse(null);
                    }

                    // Return the PostOwnerResponse with appropriate null checks
                    return dataConverter.PostOwnerResponse(
                            new PostOwnerResponse(),
                            data.getSecureId(),
                            data.getAppUserDetail().getName(),
                            data.getAppUserDetail().getAvatar(),
                            firstBusiness != null ? firstBusiness.getName() : null,
                            firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                            firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                            data,
                            userAccess
                    );
                })
                .collect(Collectors.toList());

        dtos.sort(Comparator.comparing(PostOwnerResponse::getIsFollowed).reversed());
        return PageCreateReturnApps.create(pageResult, dtos);
    }


    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataProfileSavedActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);


        String userId = GlobalConverter.getUuidUser(userRepository);

        // Retrieve likes for the userLogin
        Page<UserHasSavedPost> likesPage = userHasSavedPostRepository.findSavedPostByUserId(userId, set.pageable());

        List<PostHomeResponse> dtos = likesPage.stream()
                .map(savedPost -> {
                    Post post = savedPost.getPost();
                    PostHomeResponse dto = postConverter.convertToDetailResponse(post, userLogin);
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturnApps.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataProfileLikesActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // Set up pagination and sorting
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        String userId = GlobalConverter.getUuidUser(userRepository);
        AppUser user = GlobalConverter.getUserEntity(userRepository);

        // Retrieve likes for the user
        Page<LikeDislike> likesPage = likeDislikeRepository.findLikesByUserId(userId, set.pageable());

        List<PostHomeResponse> dtos = likesPage.stream()
                .map(likeDislike -> {
                    Post post = likeDislike.getPost();
                    PostHomeResponse dto = postConverter.convertToDetailResponse(post, user);
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturnApps.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostCommentsResponse> listDataPostCommentsActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Comment> pageResult = commentRepository.findAllCommentUser(userLogin.getId(), set.pageable());

        TreePostConverter converterPost = new TreePostConverter(baseUrl);

        Map<String, ProfileActivityPostCommentsResponse> postCommentsMap = new HashMap<>();

        pageResult.getContent().forEach(comment -> {
            Post post = comment.getPost();
            String postId = post.getSecureId();

            ProfileActivityPostCommentsResponse response = postCommentsMap.computeIfAbsent(postId, k -> {
                ProfileActivityPostCommentsResponse newResponse = new ProfileActivityPostCommentsResponse();

                newResponse.setUserId(userLogin.getSecureId());
                newResponse.setUserName(userLogin.getName());
                newResponse.setUserAvatar(GlobalConverter.getAvatarImage(userLogin.getAppUserDetail().getAvatar(), baseUrl));


                newResponse.setPost(converterPost.convertToPostHomeResponse(
                        new PostHomeResponse(),
                        post,
                        new TreePostConverter(baseUrl),
                        userLogin,
                        likeDislikeRepository
                ));
                newResponse.setComments(new ArrayList<>()); // Inisialisasi list komentar

                return newResponse;
            });

            ListCommentActivityResponse commentActivityResponse = new ListCommentActivityResponse();
            commentActivityResponse.setId(comment.getSecureId());
            commentActivityResponse.setComment(comment.getComment());
            commentActivityResponse.setCreatedAt(comment.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));

            List<ListCommentReplyResponse> commentReplies = comment.getCommentReply().stream()
                    .filter(reply -> reply.getUser().getId().equals(userLogin.getId()))
                    .map(reply -> {
                        ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
                        replyResponse.setId(reply.getSecureId());
                        replyResponse.setComment(reply.getComment());
                        replyResponse.setCreatedAt(reply.getCreatedAt().toString());
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            commentActivityResponse.setCommentReply(commentReplies);

            response.getComments().add(commentActivityResponse);
        });

        List<ProfileActivityPostCommentsResponse> dtos = new ArrayList<>(postCommentsMap.values());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

}

package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.dictionary.TreeProfileActivityConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.projection.PostCommentActivityProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.UserHasSavedPostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.service.AppUserService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.helper.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
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
        AppUser userLogin;
        if (userId == null) {
            userLogin = GlobalConverter.getUserEntity(userRepository);
        } else {
            userLogin = HandlerRepository.getEntityBySecureId(userId, userRepository, "user not found");
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
            pageResult = userRepository.findFollowing(userLogin.getId(), userAccess.getId(), set.keyword(), set.pageable());
        } else if (type.equals("FOLLOWERS")) {
            pageResult = userRepository.findFollowers(userLogin.getId(), userAccess.getId(), set.keyword(), set.pageable());
        } else {
            throw new IllegalArgumentException("Invalid type. Must be 'FOLLOWING' or 'FOLLOWERS'.");
        }

        // Map results to DTOs
        List<PostOwnerResponse> dtos = pageResult.stream()
                .map(data -> {
                    TreePostConverter dataConverter = new TreePostConverter(baseUrl, userRepository);

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
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        // Get comment form userLogin
        Page<PostCommentActivityProjection> pageResult = commentRepository.findAllActivityCommentByUser(userLogin.getSecureId(), set.pageable());

        TreeProfileActivityConverter converter = new TreeProfileActivityConverter();
        TreePostConverter postConverter = new TreePostConverter(baseUrl, userRepository);

        List<ProfileActivityPostCommentsResponse> dtos = pageResult.stream().map(result -> {
            Comment comment = result.getComment(); // Get comment
            AppUser commentUser = result.getCommentUser(); // Get userLogin create comment
            CommentReply commentReply = result.getCommentReply(); // Get commentReply
            AppUser replyUser = result.getReplyUser(); // Get userLogin create commentReply
            Post post = comment.getPost(); // Get post dari comment src

            ProfileActivityPostCommentsResponse dto = new ProfileActivityPostCommentsResponse();

            // Use TreeProfileActivityConverter for convert data
            PostHomeResponse postDto = new PostHomeResponse();
            converter.convertActivityComments(dto, commentUser, userLogin, post, comment, baseUrl, likeDislikeRepository, userRepository);

//            // Set data untuk balasan jika ada
            if (commentReply != null) {
                ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
                replyResponse.setId(commentReply.getSecureId());
                replyResponse.setComment(commentReply.getComment());

                Business firstBusiness = replyUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary).findFirst().orElse(null);
                assert firstBusiness != null;
                BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                        .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
                assert firstBusinessCategory != null;
                replyResponse.setOwner(postConverter.PostOwnerResponse(
                        new PostOwnerResponse(),
                        replyUser.getSecureId(),
                        replyUser.getAppUserDetail().getName(),
                        replyUser.getAppUserDetail().getAvatar(),
                        firstBusiness != null ? firstBusiness.getName() : null,
                        firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                        firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                        commentReply.getUser(),
                        userLogin
                ));
                replyResponse.setCreatedAt(Formatter.formatDateTimeApps(commentReply.getCreatedAt()));
            }

            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }


}

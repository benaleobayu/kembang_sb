package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.dictionary.TreeProfileActivityConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.apps.SimplePostResponse;
import com.bca.byc.model.projection.PostCommentActivityProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.UserHasSavedPostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.helper.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserProfileServiceImpl implements AppUserProfileService {

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
    public void updateUserAvatar(String email, MultipartFile avatar) throws IOException, InvalidFileTypeImageException {
        FileUploadHelper.validateFileTypeImage(avatar);

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldAvatar = userDetail.getAvatar();
        String avatarPath = FileUploadHelper.saveFile(avatar, UPLOAD_DIR + "/avatar");

        userDetail.setAvatar(avatarPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        userRepository.save(appUser);

        if (oldAvatar != null && !oldAvatar.isEmpty()) {
            FileUploadHelper.deleteFile(oldAvatar, UPLOAD_DIR);
        }
    }

    @Override
    public void updateUserCover(String email, MultipartFile cover) throws IOException, InvalidFileTypeImageException {
        FileUploadHelper.validateFileTypeImage(cover);

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AppUserDetail userDetail = appUser.getAppUserDetail();
        String oldCover = userDetail.getCover();
        String coverPath = FileUploadHelper.saveFile(cover, UPLOAD_DIR + "/cover");

        userDetail.setCover(coverPath.replace("src/main/resources/static/", "/"));
        userDetailRepository.save(userDetail);
        userRepository.save(appUser);

        if (oldCover != null && !oldCover.isEmpty()) {
            FileUploadHelper.deleteFile(oldCover, UPLOAD_DIR);
        }
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

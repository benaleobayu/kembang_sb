package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
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
        // Set up pagination and sorting
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);


        String userId = GlobalConverter.getUuidUser(userRepository);
        AppUser user = GlobalConverter.getUserEntity(userRepository);

        // Retrieve likes for the user
        Page<UserHasSavedPost> likesPage = userHasSavedPostRepository.findSavedPostByUserId(userId, set.pageable());

        List<PostHomeResponse> dtos = likesPage.stream()
                .map(savedPost -> {
                    Post post = savedPost.getPost();
                    PostHomeResponse dto = postConverter.convertToDetailResponse(post, user.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
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
                    PostHomeResponse dto = postConverter.convertToDetailResponse(post, user.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostCommentsResponse> listDataPostCommentsActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);


        String userId = GlobalConverter.getUuidUser(userRepository);

        // Get comment form user
        Page<Object[]> pageResult = commentRepository.findAllActivityCommentByUser(userId, set.pageable());

        TreeProfileActivityConverter converter = new TreeProfileActivityConverter();
        TreePostConverter postConverter = new TreePostConverter(baseUrl);

        List<ProfileActivityPostCommentsResponse> dtos = pageResult.stream().map(result -> {
            Comment comment = (Comment) result[0]; // Get comment
            AppUser commentUser = (AppUser) result[1]; // Get user create comment
            CommentReply commentReply = (CommentReply) result[2]; // Get commentReply
            AppUser replyUser = (AppUser) result[3]; // Get user create commentReply
            Post post = comment.getPost(); // Get post dari comment src

            ProfileActivityPostCommentsResponse dto = new ProfileActivityPostCommentsResponse();

            // Use TreeProfileActivityConverter for convert data
            SimplePostResponse postDto = new SimplePostResponse();
            converter.convertActivityComments(dto, commentUser, comment, postDto, baseUrl, likeDislikeRepository);

            // Set data untuk balasan jika ada
            if (commentReply != null) {
                ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
                replyResponse.setId(commentReply.getSecureId());
                replyResponse.setComment(commentReply.getComment());

                Business firstBusiness = replyUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary).findFirst().orElse(null);
                assert firstBusiness != null;
                BusinessCategory firstBusinessCategory = firstBusiness.getBusinessCategories().stream()
                        .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null);
                assert firstBusinessCategory != null;
                replyResponse.setOwner(postConverter.PostOwnerResponse(
                        new PostOwnerResponse(),
                        replyUser.getSecureId(),
                        replyUser.getAppUserDetail().getName(),
                        replyUser.getAppUserDetail().getAvatar(),
                        firstBusiness.getName(),
                        firstBusinessCategory.getName(),
                        firstBusiness.getIsPrimary()
                ));
                replyResponse.setCreatedAt(Formatter.formatDateTimeApps(commentReply.getCreatedAt()));
//                dto.getComments().get(0).setCommentReply(Collections.singletonList(replyResponse)); // Add reply
            }

            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }


}

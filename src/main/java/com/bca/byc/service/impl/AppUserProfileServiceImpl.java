package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.*;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserProfileServiceImpl implements AppUserProfileService {

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Override
    public void updateUserAvatar(String email, MultipartFile avatar) throws IOException, InvalidFileTypeException {
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
            FileUploadHelper.deleteFile(oldAvatar);
        }
    }

    @Override
    public void updateUserCover(String email, MultipartFile cover) throws IOException, InvalidFileTypeException {
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
            FileUploadHelper.deleteFile(oldCover);
        }
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostResponse> listDataProfileSavedActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String email = ContextPrincipal.getPrincipal();
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        String userId = user.getSecureId();

        Page<AppUser> pageResult = userRepository.showProfileSavedActivity(userId, pageable);

        List<ProfileActivityPostResponse> dtos = pageResult.stream().map(appUser -> {
            ProfileActivityPostResponse dto = new ProfileActivityPostResponse();

            // Get the first saved post if it exists
            Optional<UserHasSavedPost> firstSavedPostOpt = appUser.getSavedPosts().stream().findFirst();

            if (firstSavedPostOpt.isPresent()) {
                Post firstPost = firstSavedPostOpt.get().getPost(); // Access the Post through UserHasSavedPost

                // Assuming PostHasContent provides a way to get the associated PostContents
                List<PostContent> postContents = firstPost.getPostContents(); // Retrieve the list of PostContents
                if (!postContents.isEmpty()) {
                    PostContent firstContent = postContents.get(0); // Get the first PostContent
                    dto.setContentId(firstContent.getSecureId());
                    dto.setContent(firstContent.getContent()); // Set content
                    dto.setContentType(firstContent.getType()); // Set content type
                    dto.setThumbnail(firstContent.getThumbnail()); // Set thumbnail
                } else {
                    // Handle case where the post has no contents
                    dto.setContentId(null);
                    dto.setContent(null);
                    dto.setContentType(null);
                    dto.setThumbnail(null);
                }
            } else {
                // Handle case where there are no saved posts
                dto.setContentId(null);
                dto.setContent(null);
                dto.setContentType(null);
                dto.setThumbnail(null);
            }

            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostResponse> listDataProfileLikesActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String email = ContextPrincipal.getPrincipal();
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        String userId = user.getSecureId();

        Page<AppUser> pageResult = userRepository.showProfileLikesActivity(userId, pageable);

        List<ProfileActivityPostResponse> dtos = pageResult.stream().map(appUser -> {
            ProfileActivityPostResponse dto = new ProfileActivityPostResponse();

            // Get the first saved post if it exists
            Optional<UserHasSavedPost> firstSavedPostOpt = appUser.getSavedPosts().stream().findFirst();

            if (firstSavedPostOpt.isPresent()) {
                Post firstPost = firstSavedPostOpt.get().getPost(); // Access the Post through UserHasSavedPost

                // Assuming PostHasContent provides a way to get the associated PostContents
                List<PostContent> postContents = firstPost.getPostContents(); // Retrieve the list of PostContents
                if (!postContents.isEmpty()) {
                    PostContent firstContent = postContents.get(0); // Get the first PostContent
                    dto.setContentId(firstContent.getSecureId());
                    dto.setContent(firstContent.getContent()); // Set content
                    dto.setContentType(firstContent.getType()); // Set content type
                    dto.setThumbnail(firstContent.getThumbnail()); // Set thumbnail
                } else {
                    // Handle case where the post has no contents
                    dto.setContentId(null);
                    dto.setContent(null);
                    dto.setContentType(null);
                    dto.setThumbnail(null);
                }
            } else {
                // Handle case where there are no saved posts
                dto.setContentId(null);
                dto.setContent(null);
                dto.setContentType(null);
                dto.setThumbnail(null);
            }

            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }



}

package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.InvalidFileTypeException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.UserHasSavedPostRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserProfileServiceImpl implements AppUserProfileService {

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final UserHasSavedPostRepository userHasSavedPostRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Value("${app.base.url}")
    private String baseUrl;

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
        // Set up pagination and sorting
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(PaginationUtil.getSortBy(direction), sortBy);
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String email = ContextPrincipal.getPrincipal();
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        String userId = user.getSecureId();

        // Retrieve likes for the user
        Page<UserHasSavedPost> likesPage = userHasSavedPostRepository.findSavedPostByUserId(userId, pageable);

        List<ProfileActivityPostResponse> dtos = likesPage.stream()
                .map(savedPost -> {
                    ProfileActivityPostResponse dto = new ProfileActivityPostResponse();

                    Post post = savedPost.getPost();
                    TreePostConverter converter = new TreePostConverter(baseUrl);
                    converter.ProfileActivityPostResponseConverter(dto, post);

                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostResponse> listDataProfileLikesActivity(
            Integer pages,
            Integer limit,
            String sortBy,
            String direction,
            String keyword
    ) {
        // Set up pagination and sorting
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(PaginationUtil.getSortBy(direction), sortBy);
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String email = ContextPrincipal.getPrincipal();
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        String userId = user.getSecureId();

        // Retrieve likes for the user
        Page<LikeDislike> likesPage = likeDislikeRepository.findLikesByUserId(userId, pageable);

        List<ProfileActivityPostResponse> dtos = likesPage.stream()
                .map(likeDislike -> {
                    ProfileActivityPostResponse dto = new ProfileActivityPostResponse();

                    Post post = likeDislike.getPost();
                    TreePostConverter converter = new TreePostConverter(baseUrl);
                    converter.ProfileActivityPostResponseConverter(dto, post);

                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
    }


}

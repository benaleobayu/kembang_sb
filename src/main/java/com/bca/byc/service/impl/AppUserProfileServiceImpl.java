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
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.UserHasSavedPostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AppUserProfileService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.PaginationUtil;
import com.bca.byc.util.helper.Formatter;
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
import java.util.Collections;
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
            FileUploadHelper.deleteFile(oldAvatar);
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
            FileUploadHelper.deleteFile(oldCover);
        }
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataProfileSavedActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // Set up pagination and sorting
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(PaginationUtil.getSortBy(direction), sortBy);
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String userId = GlobalConverter.getUuidUser(userRepository);
        AppUser user = GlobalConverter.getUserEntity(userRepository);

        // Retrieve likes for the user
        Page<UserHasSavedPost> likesPage = userHasSavedPostRepository.findSavedPostByUserId(userId, pageable);

        List<PostHomeResponse> dtos = likesPage.stream()
                .map(savedPost -> {
                    Post post = savedPost.getPost();
                    PostHomeResponse dto = postConverter.convertToListResponse(post, user.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<PostHomeResponse> listDataProfileLikesActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // Set up pagination and sorting
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(PaginationUtil.getSortBy(direction), sortBy);
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String userId = GlobalConverter.getUuidUser(userRepository);
        AppUser user = GlobalConverter.getUserEntity(userRepository);

        // Retrieve likes for the user
        Page<LikeDislike> likesPage = likeDislikeRepository.findLikesByUserId(userId, pageable);

        List<PostHomeResponse> dtos = likesPage.stream()
                .map(likeDislike -> {
                    Post post = likeDislike.getPost();
                    PostHomeResponse dto = postConverter.convertToListResponse(post, user.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        return PageCreateReturn.create(likesPage, dtos);
    }

    @Override
    public ResultPageResponseDTO<ProfileActivityPostCommentsResponse> listDataPostCommentsActivity(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        String userId = GlobalConverter.getUuidUser(userRepository);

        // Mengambil data komentar dan pengguna dari query
        Page<Object[]> pageResult = commentRepository.findAllActivityCommentByUser(userId, pageable);

        TreeProfileActivityConverter converter = new TreeProfileActivityConverter();
        TreePostConverter postConverter = new TreePostConverter(baseUrl);

        List<ProfileActivityPostCommentsResponse> dtos = pageResult.stream().map(result -> {
            Comment comment = (Comment) result[0]; // Ambil comment
            AppUser commentUser = (AppUser) result[1]; // Ambil user yang membuat comment
            CommentReply commentReply = (CommentReply) result[2]; // Ambil commentReply
            AppUser replyUser = (AppUser) result[3]; // Ambil user yang membuat commentReply
            Post post = comment.getPost(); // Ambil post dari komentar

            ProfileActivityPostCommentsResponse dto = new ProfileActivityPostCommentsResponse();

            // Menggunakan TreeProfileActivityConverter untuk mengonversi data
            SimplePostResponse postDto = new SimplePostResponse();
            converter.convertActivityComments(dto, commentUser, comment, postDto, baseUrl);

            // Set data untuk balasan jika ada
            if (commentReply != null) {
                ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
                replyResponse.setId(commentReply.getSecureId());
                replyResponse.setComment(commentReply.getComment());
                replyResponse.setOwner(postConverter.OwnerDataResponse(
                        new OwnerDataResponse(),
                        replyUser.getSecureId(),
                        replyUser.getAppUserDetail().getName(),
                        replyUser.getAppUserDetail().getAvatar()
                ));
                replyResponse.setCreatedAt(Formatter.formatDateTimeApps(commentReply.getCreatedAt()));
                dto.getComments().get(0).setCommentReply(Collections.singletonList(replyResponse)); // Menambahkan balasan ke komentar
            }

            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }




}

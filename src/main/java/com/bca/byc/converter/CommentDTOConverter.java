package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class CommentDTOConverter {

    private final AppUserRepository userRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    @Value("${app.base.url}")
    private String baseUrl;
    private ModelMapper modelMapper;

    // for get data
    public CommentDetailResponse convertToListResponse(Comment data) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        // mapping Entity with DTO Entity
        CommentDetailResponse dto = modelMapper.map(data, CommentDetailResponse.class);
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl,userRepository);

        AppUser owner = data.getUser();
        Business firstBusiness = owner.getBusinesses().stream()
                .filter(Business::getIsPrimary).findFirst().orElse(null);
        assert firstBusiness != null;
        BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
        assert firstBusinessCategory != null;
        dto.setOwner(converter.PostOwnerResponse(
                new PostOwnerResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar(),
                firstBusiness != null ? firstBusiness.getName() : null,
                firstBusinessCategory != null  ? firstBusinessCategory.getName() : null,
                firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                owner,
                userLogin
        ));
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        boolean isOwner = Objects.equals(userLogin.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentReplyIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLike(isLike);
        // return
        return dto;
    }

     // for get data replies
    public CommentDetailResponse convertToListRepliesResponse(CommentReply data) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);
        // mapping Entity with DTO Entity
        CommentDetailResponse dto = modelMapper.map(data, CommentDetailResponse.class);
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl, userRepository);

        AppUser owner = data.getUser();
        Business firstBusiness = owner.getBusinesses().stream()
                .filter(Business::getIsPrimary).findFirst().orElse(null);
        assert firstBusiness != null;
        BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
        assert firstBusinessCategory != null;
        dto.setOwner(converter.PostOwnerResponse(
                new PostOwnerResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar(),
                firstBusiness != null ? firstBusiness.getName() : null,
                firstBusinessCategory != null  ? firstBusinessCategory.getName() : null,
                firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                data.getUser(),
                userLogin
        ));
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        boolean isOwner = Objects.equals(userLogin.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentReplyIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLike(isLike);
        dto.setParentId(data.getParentComment().getSecureId());
        // return
        return dto;
    }

    // for create data
    public Comment convertToCreateRequest(Post postData, @Valid CommentCreateUpdateRequest dto, String email) {
        // mapping DTO Entity with Entity
        Comment data = modelMapper.map(dto, Comment.class);
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        data.setUser(user);
        data.setPost(postData);
        // return
        return data;
    }

    // for update data
    public CommentReply convertToCreateReplyRequest(Post postData, CommentCreateUpdateRequest dto, String id) {
        // mapping DTO Entity with Entity
        CommentReply data = new CommentReply();
        // return
        return data;
    }

    public void convertToUpdateRequest(Comment data, @Valid CommentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setComment(dto.getComment());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}


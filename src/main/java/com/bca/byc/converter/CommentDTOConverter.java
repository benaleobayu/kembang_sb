package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.apps.*;
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

@Component
@AllArgsConstructor
public class CommentDTOConverter {

    private final AppUserRepository userRepository;
    @Value("${app.base.url}")
    private String baseUrl;
    private ModelMapper modelMapper;

    // for get data
    public ListCommentResponse convertToPageListResponse(Comment data) {
        TreePostConverter converter = new TreePostConverter(baseUrl, userRepository);
        // mapping Entity with DTO Entity
        ListCommentResponse dto = new ListCommentResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setComment(data.getComment());

        Business firstBusiness = data.getUser().getBusinesses().stream()
                .filter(Business::getIsPrimary).findFirst().orElse(null);
        assert firstBusiness != null;
        BusinessCategory firstBusinessCategory = firstBusiness.getBusinessCategories().stream()
                .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null);
        assert firstBusinessCategory != null;
        PostOwnerResponse owner = converter.PostOwnerResponse(
                new PostOwnerResponse(),
                data.getUser().getSecureId(),
                data.getUser().getName(),
                data.getUser().getAppUserDetail().getAvatar(),
                firstBusiness.getName(),
                firstBusinessCategory.getName(),
                firstBusiness.getIsPrimary(),
                null
        );
        dto.setOwner(owner);
//        dto.setOwner(owner);
        List<ListCommentReplyResponse> commentReplyResponse  = new ArrayList<>();
        for (CommentReply reply : data.getCommentReply()) {
            ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
            replyResponse.setId(reply.getSecureId());
            replyResponse.setIndex(reply.getId());
            replyResponse.setComment(reply.getComment());
            Business firstBusinessOnReply = data.getUser().getBusinesses().stream()
                    .filter(Business::getIsPrimary).findFirst().orElse(null);
            assert firstBusinessOnReply != null;
            BusinessCategory firstBusinessCategoryOnReply = firstBusiness.getBusinessCategories().stream()
                    .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null);
            assert firstBusinessCategoryOnReply != null;
            PostOwnerResponse replyOwner = converter.PostOwnerResponse(
                    new PostOwnerResponse(),
                    data.getUser().getSecureId(),
                    data.getUser().getName(),
                    data.getUser().getAppUserDetail().getAvatar(),
                    firstBusinessOnReply.getName(),
                    firstBusinessCategoryOnReply.getName(),
                    firstBusinessOnReply.getIsPrimary(),
                    null
            );
            replyResponse.setOwner(replyOwner);
            commentReplyResponse.add(replyResponse);
        }
//        dto.setCommentReply(commentReplyResponse);
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        // return
        return dto;
    }

    // for get data
    public CommentDetailResponse convertToListResponse(Comment data) {
        AppUser user = GlobalConverter.getUserEntity(userRepository);
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
                user
        ));
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        // return
        return dto;
    }

     // for get data replies
    public CommentDetailResponse convertToListRepliesResponse(CommentReply data) {
        AppUser user = GlobalConverter.getUserEntity(userRepository);
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
                user
        ));
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
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


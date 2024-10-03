package com.bca.byc.converter.parsing;

import com.bca.byc.model.apps.*;
import com.bca.byc.util.helper.Formatter;

import java.time.LocalDateTime;
import java.util.List;

public class TreePostConverter{

    private final String baseUrl;

    public TreePostConverter(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public PostOwnerResponse PostOwnerResponse(
            PostOwnerResponse dto,
            String id,
            String name,
            String avatar,
            String businessName,
            String lineOfBusiness,
            Boolean isPrimary
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(avatar != null && avatar.startsWith("uploads/") ? baseUrl + "/" + avatar : avatar);
        dto.setBusinessName(businessName);
        dto.setLineOfBusiness(lineOfBusiness);
        dto.setIsPrimary(isPrimary);

        return dto;
    }

    public PostContentDetailResponse PostContentDetailResponse(
            PostContentDetailResponse dto,
            String contentId,
            String content,
            String contentType,
            String thumbnail,
            List<OwnerDataResponse> tagsUser
    ) {
        dto.setContentId(contentId);
        dto.setContent(content != null && (content.startsWith("uploads/") || content.startsWith("/uploads/"))
                ? baseUrl + content.replaceFirst("^/", "/")
                : content);
        dto.setContentType(contentType);
        dto.setThumbnail(thumbnail);
        dto.setContentTagsUser(tagsUser);
        return dto;
    }

    public OwnerDataResponse OwnerDataResponse(
            OwnerDataResponse dto,
            String id,
            String name,
            String avatar
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(avatar != null && avatar.startsWith("uploads/") ? baseUrl + "/" + avatar : avatar);
        return dto;
    }

    public ListCommentResponse convertToListCommentResponse(
            ListCommentResponse dto,
            String secureId,
            String comment,
//            List<ListCommentReplyResponse> commentReply,
            OwnerDataResponse owner,
            String createdAt

    ) {
        dto.setId(secureId);
        dto.setComment(comment);
//        dto.setCommentReply(commentReply);
        dto.setOwner(owner);
        dto.setCreatedAt(createdAt);
        return dto;
    }

    public ListCommentReplyResponse convertToListCommentReplyResponse(
            ListCommentReplyResponse dto,
            String secureId,
            Long index,
            String comment,
            OwnerDataResponse owner,
            LocalDateTime createdAt

    ) {
        dto.setId(secureId);
        dto.setIndex(index);
        dto.setComment(comment);
        dto.setOwner(owner);
        dto.setCreatedAt(createdAt != null ? Formatter.formatDateTimeApps(createdAt) : "No data");
        return dto;
    }
}

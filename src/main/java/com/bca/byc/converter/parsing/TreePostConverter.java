package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.util.helper.Formatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreePostConverter {

    private final String baseUrl;

    public TreePostConverter(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public OwnerDataResponse OwnerDataResponse(
            OwnerDataResponse dto,
            String id,
            String name,
            String avatar
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(GlobalConverter.getParseImage(avatar, baseUrl));
        return dto;
    }

    public ListCommentResponse convertToListCommentResponse(
            ListCommentResponse dto,
            String secureId,
            Long index,
            String comment,
            List<CommentReply> commentReply,
            AppUser owner,
            LocalDateTime createdAt

    ) {
        dto.setId(secureId);
        dto.setIndex(index);
        dto.setComment(comment);
        List<ListCommentReplyResponse> commentReplyResponse = new ArrayList<>();
        for (CommentReply data : commentReply) {
            commentReplyResponse.add(convertToListCommentReplyResponse(
                    new ListCommentReplyResponse(),
                    data.getSecureId(),
                    data.getId(),
                    data.getComment(),
                    data.getUser(),
                    data.getCreatedAt()
            ));
        }
        dto.setCommentReply(commentReplyResponse);

        dto.setOwner(OwnerDataResponse(
                new OwnerDataResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar()
        ));
        dto.setCreatedAt(createdAt != null ? Formatter.formatDateTimeApps(createdAt) : null);
        return dto;
    }

    public ListCommentReplyResponse convertToListCommentReplyResponse(
            ListCommentReplyResponse dto,
            String secureId,
            Long index,
            String comment,
            AppUser owner,
            LocalDateTime createdAt

    ) {
        dto.setId(secureId);
        dto.setIndex(index);
        dto.setComment(comment);
        dto.setOwner(OwnerDataResponse(
                new OwnerDataResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar()
        ));
        dto.setCreatedAt(createdAt != null ? Formatter.formatDateTimeApps(createdAt) : "No data");
        return dto;
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
        dto.setAvatar(GlobalConverter.getParseImage(avatar, baseUrl));
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
        dto.setContent(GlobalConverter.getParseImage(content, baseUrl));
        dto.setContentType(contentType);
        dto.setThumbnail(thumbnail);
        dto.setContentTagsUser(tagsUser);
        return dto;
    }

    public void ProfileActivityPostResponseConverter(
            ProfileActivityPostResponse dto,
            Post post
    ) {
        List<PostContent> postContents = post.getPostContents();

        if (!postContents.isEmpty()) {
            PostContent firstContent = postContents.get(0);
            dto.setContentId(firstContent.getSecureId());
            dto.setContent(GlobalConverter.getParseImage(firstContent.getContent(), baseUrl));
            dto.setContentType(firstContent.getType());
            dto.setThumbnail(firstContent.getThumbnail());
        } else {
            dto.setContentId(null);
            dto.setContent(null);
            dto.setContentType(null);
            dto.setThumbnail(null);
        }

    }

    public Post countPostComments(Post post, PostRepository repository, String type) {
        if (type.equals("add")) {
            post.setCommentsCount(post.getCommentsCount() + 1);
        } else if (type.equals("delete")) {
            post.setCommentsCount(post.getCommentsCount() - 1);
        }
        return repository.save(post);
    }
}

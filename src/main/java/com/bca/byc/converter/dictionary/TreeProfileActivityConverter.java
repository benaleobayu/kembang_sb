package com.bca.byc.converter.dictionary;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.util.helper.Formatter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.bca.byc.converter.parsing.GlobalConverter.getParseImage;

public class TreeProfileActivityConverter {

    private SimplePostResponse convertPostData(
            SimplePostResponse dto,
            AppUser user,
            String baseUrl
    ) {
        PostContent firstPostContent = user.getPosts().getFirst().getPostContents().getFirst();

        dto.setPostId(user.getPosts().getFirst().getSecureId());
        dto.setPostDescription(user.getPosts().getFirst().getDescription());
        dto.setPostContent(GlobalConverter.getParseImage(firstPostContent.getContent(), baseUrl));
        dto.setPostThumbnail(GlobalConverter.getParseImage(firstPostContent.getThumbnail(), baseUrl));
        dto.setPostCreatedAt(user.getPosts().getFirst().getCreatedAt() != null ? Formatter.formatDateTimeApps(user.getPosts().getFirst().getCreatedAt()) : null);
        return dto;
    }

    public ProfileActivityPostCommentsResponse convertActivityComments(
            ProfileActivityPostCommentsResponse dto,
            AppUser user,
            AppUser userLogin,
            Post post,
            Comment comment,
            String baseUrl,
            LikeDislikeRepository likeDislikeRepository
    ) {
        TreePostConverter converter = new TreePostConverter(baseUrl);
        dto.setUserId(user.getSecureId());
        dto.setUserName(user.getAppUserDetail().getName());
        dto.setUserAvatar(getParseImage(user.getAppUserDetail().getAvatar(), baseUrl));
        dto.setPost(converter.convertToPostHomeResponse(
                new PostHomeResponse(),
                post,
                converter,
                userLogin,
                likeDislikeRepository
        ));
        List<ListCommentActivityResponse> comments = new ArrayList<>();
        for (Comment c : post.getComments()) {
            comments.add(convertToListCommentResponse(
                    new ListCommentActivityResponse(),
                    user,
                    c,
                    baseUrl,
                    likeDislikeRepository
            ));
        }
        dto.setComments(comments);
        return dto;
    }

    // list comment
    public ListCommentActivityResponse convertToListCommentResponse(
            ListCommentActivityResponse dto,
            AppUser userLogin,
            Comment data,
            String baseUrl,
            LikeDislikeRepository likeDislikeRepository
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl);

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
                firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                data.getUser(),
                userLogin
        ));
        dto.setCreatedAt(data.getCreatedAt() != null ? data.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null);

        boolean isOwner = Objects.equals(userLogin.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLike(isLike);
        dto.setLikeCount(Math.toIntExact(data.getLikesCount()));
        dto.setRepliesCount(data.getCommentReply().size());

        return dto;
    }

}

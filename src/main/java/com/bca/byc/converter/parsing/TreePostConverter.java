package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCategoryResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TreePostConverter {

    private final String baseUrl;

    private final AppUserRepository userRepository;

    public TreePostConverter(String baseUrl, AppUserRepository userRepository) {
        this.baseUrl = baseUrl;
        this.userRepository = userRepository;
    }

    public PostHomeResponse convertToPostHomeResponse(
            PostHomeResponse dto,
            Post data,
            TreePostConverter converter,
            AppUser user,
            LikeDislikeRepository likeDislikeRepository
    ){

        dto.setPostId(data.getSecureId());
        dto.setPostDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());
        dto.setPostAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        AppUser appUser = data.getUser();

        dto.setPostTagsList(convertTagList(data.getTags()));
        dto.setPostContentList(convertPostContents(data.getPostContents(), converter));
        dto.setPostOwner(convertOwnerDataWithBusiness(converter, appUser));
        dto.setPostCategory(convertPostCategory(data.getPostCategory()));

        // check is my post or not
        dto.setIsMyPost(Objects.equals(appUser.getId(), user.getId()));

        // Check if the post is liked by the user
        boolean isLiked = likeDislikeRepository.findByPostIdAndUserId(data.getId(), user.getId()).isPresent();
        dto.setIsLiked(isLiked);

        // Check if the user is following the post owner
        boolean isFollowing = data.getUser().getFollowers().stream().anyMatch(f -> f.getId().equals(user.getId()));
        dto.setIsFollowed(isFollowing);

        dto.setIsOfficial(data.getUser().getAppUserAttribute().getIsOfficial());

        String officialUrl = data.getUser().getAppUserAttribute().getOfficialUrl();
        dto.setOfficialUrl(officialUrl);

        dto.setLikeCount(data.getLikesCount());
        dto.setCommentCount(data.getCommentsCount());
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
        dto.setAvatar(GlobalConverter.getAvatarImage(avatar, baseUrl));
        return dto;
    }

    public ListCommentResponse convertToListCommentResponse(
            ListCommentResponse dto,
            AppUser user,
            Comment data,
            LikeDislikeRepository likeDislikeRepository
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setComment(data.getComment());
        List<ListCommentReplyResponse> commentReplyResponse = new ArrayList<>();
//        commentReply.stream()
//                .sorted(Comparator.comparing(CommentReply::getCreatedAt).reversed())
//                .limit(3)
//                .forEach(data -> commentReplyResponse.add(convertToListCommentReplyResponse(
//                        new ListCommentReplyResponse(),
//                        data.getSecureId(),
//                        data.getId(),
//                        data.getComment(),
//                        data.getUser(),
//                        data.getCreatedAt()
//                )));
//        dto.setCommentReply(commentReplyResponse);
//        int lastPage = (int) Math.floor(commentReply.size() / 10.0);
//        int page = commentReply.size() < 10 ? 0 : lastPage;
//        dto.setCommentReplyLastPage(page);

        AppUser owner = data.getUser();
        Business firstBusiness = owner.getBusinesses().stream()
                .filter(Business::getIsPrimary).findFirst().orElse(null);
        assert firstBusiness != null;
        BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                        .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
        assert firstBusinessCategory != null;
        dto.setOwner(PostOwnerResponse(
                new PostOwnerResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar(),
                firstBusiness != null ? firstBusiness.getName() : null,
                firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                user
        ));
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatterAppsWithSeconds(data.getCreatedAt()) : null);

        boolean isOwner = Objects.equals(user.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentIdAndUserId(data.getId(), user.getId()).isPresent();
        dto.setIsLike(isLike);
        dto.setLikeCount(Math.toIntExact(data.getLikesCount()));
        dto.setRepliesCount(data.getCommentReply().size());

        return dto;
    }

    public ListCommentReplyResponse convertToListCommentReplyResponse(
            ListCommentReplyResponse dto,
            CommentReply data,
            AppUser user,
            LikeDislikeRepository likeDislikeRepository
    ) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setComment(data.getComment());

        AppUser owner = data.getUser();
        Business firstBusiness = owner.getBusinesses().stream()
                .filter(Business::getIsPrimary).findFirst().orElse(null);
        assert firstBusiness != null;
        BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
        assert firstBusinessCategory != null;
        dto.setOwner(PostOwnerResponse(
                new PostOwnerResponse(),
                owner.getSecureId(),
                owner.getAppUserDetail().getName(),
                owner.getAppUserDetail().getAvatar(),
                firstBusiness != null ? firstBusiness.getName() : null,
                firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                user
        ));
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatterAppsWithSeconds(data.getCreatedAt()) : "No data");
        boolean isOwner = Objects.equals(user.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentReplyIdAndUserId(data.getId(), user.getId()).isPresent();
        dto.setIsLike(isLike);
        dto.setLikeCount(Math.toIntExact(data.getLikesCount()));
        dto.setParentId(data.getParentComment().getSecureId());
        return dto;
    }
    // Helper owner with business
    public PostOwnerResponse convertOwnerDataWithBusiness(TreePostConverter converter, AppUser appUser) {
        return converter.PostOwnerResponse(
                new PostOwnerResponse(),
                appUser.getSecureId(),
                appUser.getAppUserDetail().getName(),
                appUser.getAppUserDetail().getAvatar(),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .map(Business::getName)
                        .findFirst().orElse(null),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .map(b -> b.getBusinessCategories().stream()
                                .map(bc -> bc.getBusinessCategoryParent().getName())
                                .findFirst().orElse(""))
                        .findFirst().orElse(null),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .map(business -> true)
                        .findFirst().orElse(false),
                appUser
        );
    }
    public PostOwnerResponse PostOwnerResponse(PostOwnerResponse dto, String id, String name, String avatar, String businessName, String lineOfBusiness, Boolean isPrimary, AppUser user
    ) {
        AppUser owner = HandlerRepository.getIdBySecureId(
                id,
                userRepository::findBySecureId,
                projection -> userRepository.findById(projection.getId()),
                "User not found"
        );
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(GlobalConverter.getAvatarImage(avatar, baseUrl));
        boolean isMyAccount = Objects.equals(user.getSecureId(), id);
        dto.setIsMyAccount(isMyAccount);
        boolean isFollowing = user.getFollows().stream().anyMatch(f -> f.getId().equals(owner.getId()));
        dto.setIsFollowed(isFollowing);
        dto.setBusinessName(businessName);
        dto.setLineOfBusiness(lineOfBusiness);
        dto.setIsPrimary(isPrimary);

        return dto;
    }

    public PostContentDetailResponse PostContentDetailResponse(PostContentDetailResponse dto, String contentId, String content, String contentType, String thumbnail, List<OwnerDataResponse> tagsUser
    ) {
        dto.setContentId(contentId);
        dto.setContent(GlobalConverter.getParseImage(content, baseUrl));
        dto.setContentType(contentType);
        if (contentType.equals("image")){
            dto.setThumbnail(GlobalConverter.getParseImage(content, baseUrl));
        } else {
            dto.setThumbnail(GlobalConverter.getParseImage(thumbnail, baseUrl));
        }
        dto.setContentTagsUser(tagsUser);
        return dto;
    }

    public void ProfileActivityPostResponseConverter(ProfileActivityPostResponse dto, Post post
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


    // Helper tag list
    public List<String> convertTagList(Set<Tag> tagList) {
        return tagList.stream().map(Tag::getName).collect(Collectors.toList());
    }

    // Helper post content
    public List<PostContentDetailResponse> convertPostContents(List<PostContent> postContentList, TreePostConverter converter) {
        return postContentList.stream().map(postContent -> {
            List<OwnerDataResponse> tagUsers = postContent.getTagUsers().stream()
                    .map(tagUser -> converter.OwnerDataResponse(
                            new OwnerDataResponse(),
                            tagUser.getSecureId(),
                            tagUser.getAppUserDetail().getName(),
                            tagUser.getAppUserDetail().getAvatar()
                    )).collect(Collectors.toList());
            return converter.PostContentDetailResponse(
                    new PostContentDetailResponse(),
                    postContent.getSecureId(),
                    postContent.getContent(),
                    postContent.getType(),
                    postContent.getThumbnail(),
                    tagUsers
            );
        }).collect(Collectors.toList());
    }

    // Helper post category
    public PostCategoryResponse convertPostCategory(BusinessCategory data ) {
        PostCategoryResponse dto = new PostCategoryResponse();
        dto.setPostCategoryId(data.getSecureId());
        dto.setPostCategoryName(data.getName());
        return dto;
    }
}

package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCategoryResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.PostLocationRequestAndResponse;
import com.bca.byc.model.ProfileActivityPostResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.util.helper.Formatter;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TreePostConverter {

    private final String baseUrl;

    public TreePostConverter(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public PostHomeResponse convertToPostHomeResponse(
            PostHomeResponse dto,
            Post data,
            TreePostConverter converter,
            AppUser userLogin,
            LikeDislikeRepository likeDislikeRepository
    ){

        dto.setPostId(data.getSecureId());
        dto.setPostDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());
        dto.setPostAt(data.getCreatedAt() != null ? data.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null);
        if (data.getIsAdminPost() == null) {
            dto.setPostOwner(convertOwnerDataWithBusiness(converter, data.getUser(), userLogin));
            // check is my post or not
            dto.setIsMyPost(Objects.equals(data.getUser().getId(), userLogin.getId()));
        }

        dto.setPostTagsList(convertTagList(data.getTags()));
        dto.setPostContentList(convertPostContents(data.getPostContents(), converter, userLogin));
        dto.setPostCategory(data.getPostCategory() == null ? null :convertPostCategory(data.getPostCategory()));
        dto.setPostLocation(data.getPostLocation() == null ? null : convertPostLocation(data.getPostLocation()));

        // Check if the post is liked by the userLogin
        boolean isLiked = likeDislikeRepository.findByPostIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLiked(isLiked);

        // Check if the userLogin is following the post owner
        boolean isFollowing = data.getUser().getFollowers().stream().anyMatch(f -> f.getId().equals(userLogin.getId()));
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
                data.getUser(),
                user
        ));
        dto.setCreatedAt(data.getCreatedAt() != null ? data.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME) : null);

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
            AppUser userLogin,
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
                data.getUser(),
                userLogin
        ));
        dto.setCreatedAt(data.getCreatedAt() != null ? Formatter.formatterAppsWithSeconds(data.getCreatedAt()) : "No data");
        boolean isOwner = Objects.equals(userLogin.getId(), owner.getId());
        dto.setIsOwnerPost(isOwner);
        boolean isLike = likeDislikeRepository.findByCommentReplyIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLike(isLike);
        dto.setLikeCount(Math.toIntExact(data.getLikesCount()));
        dto.setParentId(data.getParentComment().getSecureId());
        return dto;
    }
    // Helper owner with business
    public PostOwnerResponse convertOwnerDataWithBusiness(TreePostConverter converter, AppUser appUser, AppUser userLogin) {
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
                appUser,
                userLogin
        );
    }
    public PostOwnerResponse PostOwnerResponse(PostOwnerResponse dto,
                                               String id,
                                               String name,
                                               String avatar,
                                               String businessName,
                                               String lineOfBusiness,
                                               Boolean isPrimary,
                                               AppUser userPost,
                                               AppUser userLogin
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(GlobalConverter.getAvatarImage(avatar, baseUrl));
        boolean isMyAccount = Objects.equals(userPost.getId(), userLogin.getId());
        dto.setIsMyAccount(isMyAccount);
        boolean isFollowing = userLogin.getFollows().stream().anyMatch(f -> f.getId().equals(userPost.getId()));
        dto.setIsFollowed(isFollowing);
        dto.setBusinessName(businessName != null ? businessName : "");
        dto.setLineOfBusiness(lineOfBusiness);
        dto.setIsPrimary(isPrimary);
        return dto;
    }
    public PostOwnerResponse TagUserResponse(PostOwnerResponse dto,
                                               String id,
                                               String name,
                                               String avatar,
                                               String businessName,
                                               String lineOfBusiness,
                                               Boolean isPrimary,
                                               AppUser userTagged,
                                               AppUser userLogin
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(GlobalConverter.getAvatarImage(avatar, baseUrl));
        boolean isMyAccount = Objects.equals(userTagged.getId(), userLogin.getId());
        dto.setIsMyAccount(isMyAccount);
        boolean isFollowing = userLogin.getFollows().stream().anyMatch(f -> f.getId().equals(userTagged.getId()));
        dto.setIsFollowed(isFollowing);
        dto.setBusinessName(businessName);
        dto.setLineOfBusiness(lineOfBusiness);
        dto.setIsPrimary(isPrimary);
        return dto;
    }


    public PostContentDetailResponse PostContentDetailResponse(PostContentDetailResponse dto, String contentId, String content, String contentType, String thumbnail, List<PostOwnerResponse> tagsUser
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
    public List<PostContentDetailResponse> convertPostContents(List<PostContent> postContentList, TreePostConverter converter, AppUser userLogin) {
        return postContentList.stream().map(postContent -> {
            AppUser owner = postContent.getPost().getUser();
            Business firstBusiness = owner.getBusinesses().stream()
                    .filter(Business::getIsPrimary).findFirst().orElse(null);
            assert firstBusiness != null;
            BusinessCategory firstBusinessCategory = firstBusiness != null ? firstBusiness.getBusinessCategories().stream()
                    .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null) : null;
            assert firstBusinessCategory != null;

            List<PostOwnerResponse> tagUsers = postContent.getTagUsers().stream()
                    .map(tagUser -> converter.TagUserResponse(
                            new PostOwnerResponse(),
                            tagUser.getSecureId(),
                            tagUser.getAppUserDetail().getName(),
                            tagUser.getAppUserDetail().getAvatar(),
                            firstBusiness != null ? firstBusiness.getName() : null,
                            firstBusinessCategory != null ? firstBusinessCategory.getName() : null,
                            firstBusiness != null ? firstBusiness.getIsPrimary() : null,
                            tagUser,
                            userLogin
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

    public String getContentTypePost(List<PostContent> contentList){
        String contentType = "text";

        boolean hasImage = contentList.stream().anyMatch(pc -> pc.getType().contains("image"));
        boolean hasVideo = contentList.stream().anyMatch(pc -> pc.getType().contains("video"));

        if (hasImage) {
            contentType = "image";
        } else if (hasVideo) {
            contentType = "video";
        }
        return contentType;
    }

    public PostLocationRequestAndResponse convertPostLocation(PostLocation postLocation) {

        PostLocationRequestAndResponse dto = new PostLocationRequestAndResponse();
        dto.setPlaceName(postLocation.getPlaceName() == null ? null : postLocation.getPlaceName());
        dto.setDescription(postLocation.getDescription() == null ? null : postLocation.getDescription());
        dto.setPlaceId(postLocation.getPlaceId() == null ? null : postLocation.getPlaceId());
        dto.setLongitude(postLocation.getLongitude() == null ? null : postLocation.getLongitude());
        dto.setLatitude(postLocation.getLatitude() == null ? null : postLocation.getLatitude());
        return dto;
    }

}

package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.*;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;


@Component
@RequiredArgsConstructor
public class PostDTOConverter {
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final PostLocationRepository postLocationRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final CommentRepository commentRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    // for get data
    public PostHomeResponse convertToListResponse(Post data, Long userId) {
        // mapping Entity with DTO Entity
        PostHomeResponse dto = new PostHomeResponse();
        TreePostConverter converter = new TreePostConverter(baseUrl);

        dto.setPostId(data.getSecureId());
        dto.setPostDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());
        dto.setPostAt(data.getCreatedAt() != null ? Formatter.formatLocalDateTime(data.getCreatedAt()) : null);
        AppUser appUser = data.getUser();

        dto.setPostTagsList(converter.convertTagList(data.getTags()));
        dto.setPostHighlightsList(GlobalConverter.convertListToArray(data.getHighlight()));
        dto.setPostContentList(converter.convertPostContents(data.getPostContents(), converter));
        dto.setPostOwner(converter.convertOwnerDataWithBusiness(converter, appUser));

        // Check if the post is liked by the user
        boolean isLiked = likeDislikeRepository.findByPostIdAndUserId(data.getId(), userId).isPresent();
        dto.setIsLiked(isLiked);

        // Check if the user is following the post owner
        boolean isFollowing = data.getUser().getFollowers().stream().anyMatch(f -> f.getId().equals(userId));
        dto.setIsFollowed(isFollowing);

        dto.setIsOfficial(data.getUser().getAppUserAttribute().getIsOfficial());

        dto.setLikeCount(data.getLikesCount());
        dto.setCommentCount(data.getCommentsCount());

        // return
        return dto;
    }

    public PostHomeResponse convertToDetailResponse(Post data) {
        // mapping Entity with DTO Entity
        PostHomeResponse dto = modelMapper.map(data, PostHomeResponse.class);
        TreePostConverter converter = new TreePostConverter(baseUrl);

        dto.setPostId(data.getSecureId());
        dto.setPostDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());

        AppUser appUser = data.getUser();

        dto.setPostContentList(converter.convertPostContents(data.getPostContents(), converter));
        dto.setPostOwner(converter.convertOwnerDataWithBusiness(converter, appUser));

//        dto.setCommentList(convertComments(data.getComments(), converter));
//        dto.setIsLiked(data.getLikeDislikes().stream().anyMatch(l -> l.getUser().getId().equals(appUser.getId())));

        // return
        return dto;
    }

    // for create data
    public Post convertToCreateRequest(AppUser user, @Valid PostCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Post data = new Post();
        data.setDescription(dto.getDescription());
        data.setId(null);
        data.setUser(user);

        // Set list of Tags
        Set<Tag> tags = new HashSet<>();
        if (dto.getTagName() != null) {
            for (String tagName : dto.getTagName()) {
                Optional<Tag> tag = tagRepository.findByName(tagName);
                tag.ifPresentOrElse(tags::add, () -> {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    tags.add(tagRepository.save(newTag));
                });
            }
        }
        data.setTags(tags);

        // Post category
        if (dto.getPostCategoryId() != null) {
            BusinessCategory postCategory = HandlerRepository.getIdBySecureId(
                    dto.getPostCategoryId(),
                    businessCategoryRepository::findBySecureId,
                    projection -> businessCategoryRepository.findById(projection.getId()),
                    "Post category not found"
            );
            data.setPostCategory(postCategory);
        }

        // Post location
        if (dto.getPostLocation() != null) {
            String placeName = dto.getPostLocation().getPlaceName();
            if (placeName != null && !placeName.isEmpty()) {
                PostLocation postLocation = postLocationRepository.findByPlaceName(placeName);
                if (postLocation == null) {
                    postLocation = new PostLocation();
                    postLocation.setPlaceName(placeName);
                    postLocation.setPlaceId(dto.getPostLocation().getPlaceId());
                    postLocation.setDescription(dto.getPostLocation().getDescription());
                    postLocation.setLatitude(dto.getPostLocation().getLatitude());
                    postLocation.setLongitude(dto.getPostLocation().getLongitude());
                    postLocation = postLocationRepository.save(postLocation);
                }
                data.setPostLocation(postLocation);
            } else {
                // Handle empty placeName if necessary
                data.setPostLocation(null); // or skip setting it
            }
        } else {
            data.setPostLocation(null); // Explicitly set to null if no location provided
        }

        // attribute
        data.setIsPosted(dto.getIsPosted());
        data.setIsCommentable(dto.getIsCommentable());
        data.setIsShareable(dto.getIsShareable());
        data.setIsShowLikes(dto.getIsShowLikes());

        data.setPostAt(LocalDateTime.now());
        data.setCreatedAt(LocalDateTime.now());

        return data;
    }

    // for update data
    public void convertToUpdateRequest(Post data, @Valid PostCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

    // -----------------


    private OwnerDataResponse convertOwnerData(TreePostConverter converter, AppUser appUser) {
        return converter.OwnerDataResponse(
                new OwnerDataResponse(),
                appUser.getSecureId(),
                appUser.getAppUserDetail().getName(),
                appUser.getAppUserDetail().getAvatar()
        );
    }

    public ProfilePostResponse convertToProfilePostResponse(AppUser data) {
        ProfilePostResponse dto = modelMapper.map(data, ProfilePostResponse.class);
        dto.setId(data.getSecureId());
        dto.setContent(data.getPosts().stream()
                .map(p -> p.getPostContents().stream().findFirst().map(PostContent::getContent).orElse(""))
                .findFirst().orElse(null)
        );
        return dto;

    }

    // Helper method to convert comment replies
    private List<ListCommentReplyResponse> convertCommentReplies(TreePostConverter converter, List<CommentReply> commentReplies) {
        List<ListCommentReplyResponse> replyResponses = new ArrayList<>();
        for (CommentReply reply : commentReplies) {
            ListCommentReplyResponse replyResponse = new ListCommentReplyResponse();
            replyResponse.setId(reply.getSecureId());
            replyResponse.setComment(reply.getComment());

            Business firstBusiness = reply.getUser().getBusinesses().stream()
                    .filter(Business::getIsPrimary).findFirst().orElse(null);
            assert firstBusiness != null;
            BusinessCategory firstBusinessCategory = firstBusiness.getBusinessCategories().stream()
                    .findFirst().map(BusinessHasCategory::getBusinessCategoryParent).orElse(null);
            assert firstBusinessCategory != null;
            replyResponse.setOwner(converter.PostOwnerResponse(
                    new PostOwnerResponse(),
                    reply.getUser().getSecureId(),
                    reply.getUser().getName(),
                    reply.getUser().getAppUserDetail().getAvatar(),
                    firstBusiness.getName(),
                    firstBusinessCategory.getName(),
                    firstBusiness.getIsPrimary()
            )); // Ensure correct conversion of the owner
            replyResponse.setCreatedAt(reply.getCreatedAt().toString()); // Convert LocalDateTime to String if needed
            replyResponses.add(replyResponse);
        }
        return replyResponses;
    }



}
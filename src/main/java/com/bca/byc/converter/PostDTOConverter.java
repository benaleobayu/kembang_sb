package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.PostCategoryRepository;
import com.bca.byc.repository.PostLocationRepository;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostDTOConverter {
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final PostLocationRepository postLocationRepository;
    private final PostCategoryRepository postCategoryRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    // for get data
    public PostHomeResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        PostHomeResponse dto = new PostHomeResponse();
        UserManagementConverter converter = new UserManagementConverter(baseUrl);

        dto.setPostId(data.getSecureId());
        dto.setPostDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());
        AppUser appUser = data.getUser();

        dto.setPostContentList(convertPostContents(data.getPostContents(), converter));
        dto.setPostOwner(convertOwnerDataWithBusiness(converter, appUser));

        dto.setIsLiked(data.getLikeDislikes().stream().anyMatch(l -> l.getUser().getId().equals(appUser.getId())));
        // return
        return dto;
    }

    public PostDetailResponse convertToDetailResponse(Post data) {
        // mapping Entity with DTO Entity
        PostDetailResponse dto = modelMapper.map(data, PostDetailResponse.class);
        UserManagementConverter converter = new UserManagementConverter(baseUrl);

        dto.setId(data.getSecureId());
        dto.setDescription(data.getDescription());
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());

        AppUser appUser = data.getUser();

        dto.setContentList(convertPostContents(data.getPostContents(), converter));
        dto.setPostOwner(convertOwnerData(converter, appUser));

        dto.setCommentList(convertComments(data.getComments(), converter));
        dto.setIsLiked(data.getLikeDislikes().stream().anyMatch(l -> l.getUser().getId().equals(appUser.getId())));



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
            PostCategory postCategory = postCategoryRepository.findById(Long.valueOf(dto.getPostCategoryId())).orElse(null);
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
    private List<PostContentDetailResponse> convertPostContents(List<PostContent> postContentList, UserManagementConverter converter) {
        return postContentList.stream().map(postContent -> {
            List<OwnerDataResponse> tagUsers = postContent.getTagUsers().stream()
                    .map(tagUser -> converter.OwnerDataResponse(
                            new OwnerDataResponse(),
                            tagUser.getSecureId(),
                            tagUser.getName(),
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

    private OwnerDataResponse convertOwnerData(UserManagementConverter converter, AppUser appUser) {
        return converter.OwnerDataResponse(
                new OwnerDataResponse(),
                appUser.getSecureId(),
                appUser.getName(),
                appUser.getAppUserDetail().getAvatar()
        );
    }

    private PostOwnerResponse convertOwnerDataWithBusiness(UserManagementConverter converter, AppUser appUser) {
        return converter.PostOwnerResponse(
                new PostOwnerResponse(),
                appUser.getSecureId(),
                appUser.getName(),
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
                        .findFirst().orElse(false)
        );
    }

    private List<ListCommentResponse> convertComments(List<Comment> comments, UserManagementConverter converter) {
        return comments.stream().map(comment -> {
            List<ListCommentReplyResponse> replies = comment.getCommentReply().stream()
                    .map(reply -> converter.convertToListCommentReplyResponse(
                            new ListCommentReplyResponse(),
                            reply.getSecureId(),
                            reply.getContent(),
                            converter.OwnerDataResponse(
                                    new OwnerDataResponse(),
                                    reply.getUser().getSecureId(),
                                    reply.getUser().getName(),
                                    reply.getUser().getAppUserDetail().getAvatar()
                            ),
                            Formatter.formatDateTimeApps(reply.getCreatedAt())
                    )).collect(Collectors.toList());
            return new ListCommentResponse(
                    comment.getSecureId(),
                    comment.getContent(),
                    replies,
                    converter.OwnerDataResponse(
                            new OwnerDataResponse(),
                            comment.getUser().getSecureId(),
                            comment.getUser().getName(),
                            comment.getUser().getAppUserDetail().getAvatar()
                    ),
                    Formatter.formatDateTimeApps(comment.getCreatedAt())
            );
        }).collect(Collectors.toList());
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
}
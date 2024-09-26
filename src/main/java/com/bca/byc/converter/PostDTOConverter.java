package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.OwnerDataResponse;
import com.bca.byc.model.apps.PostContentDetailResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
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

        // set list of post content
        List<PostContentDetailResponse> posts = new ArrayList<>();
        for (PostContent postContent : data.getPostContents()) {
            PostContentDetailResponse postContentDetailResponse = converter.PostContentDetailResponse(
                    new PostContentDetailResponse(),
                    postContent.getId(),
                    postContent.getContent(),
                    postContent.getType(),
                    postContent.getThumbnail(),
                    postContent.getTagUsers().stream().map(tagUser -> converter.OwnerDataResponse(
                            new OwnerDataResponse(),
                            tagUser.getId(),
                            tagUser.getAppUserDetail().getName(),
                            tagUser.getAppUserDetail().getAvatar()
                    )).collect(Collectors.toList())
            );
            posts.add(postContentDetailResponse);
        }
        dto.setPostContentList(posts);

        List<String> tags = new ArrayList<>();
        for (Tag tag : data.getTags()) {
            tags.add(tag.getName());
        }
        dto.setPostTagsList(tags);

        AppUser appUser = data.getUser();
        PostOwnerResponse owner = converter.PostOwnerResponse(
                dto.getPostOwner() != null ? dto.getPostOwner() : new PostOwnerResponse(),
                appUser.getId(),
                appUser.getName(),
                appUser.getAppUserDetail().getAvatar(),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .findFirst()
                        .map(Business::getName).orElse(null),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .findFirst()
                        .map(business -> business.getBusinessCategories().stream()
                                .findFirst()
                                .get().getBusinessCategoryParent().getName()).orElse(null),
                appUser.getBusinesses().stream()
                        .filter(Business::getIsPrimary)
                        .findFirst()
                        .map(Business::getIsPrimary).isPresent());
        dto.setPostOwner(owner);
        dto.setIsCommentable(data.getIsCommentable());
        dto.setIsShareable(data.getIsShareable());
        dto.setIsShowLikes(data.getIsShowLikes());
        dto.setIsPosted(data.getIsPosted());
        dto.setPostAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        // check on LikeDislikeRepository about post and user
        dto.setIsLiked(data.getLikeDislikes().stream().anyMatch(l -> l.getUser().getId().equals(appUser.getId())));
        // return
        return dto;
    }

    public PostDetailResponse convertToDetailResponse(Post data) {
        // mapping Entity with DTO Entity
        PostDetailResponse dto = modelMapper.map(data, PostDetailResponse.class);
        UserManagementConverter converter = new UserManagementConverter(baseUrl);

        dto.setId(data.getSecureId());
        AppUser appUser = data.getUser();

        List<PostContentDetailResponse> posts = new ArrayList<>();
        for (PostContent postContent : data.getPostContents()) {
            PostContentDetailResponse postContentDetailResponse = converter.PostContentDetailResponse(
                    new PostContentDetailResponse(),
                    postContent.getId(),
                    postContent.getContent(),
                    postContent.getType(),
                    postContent.getThumbnail(),
                    postContent.getTagUsers().stream().map(tagUser -> converter.OwnerDataResponse(
                            new OwnerDataResponse(),
                            tagUser.getId(),
                            tagUser.getAppUserDetail().getName(),
                            tagUser.getAppUserDetail().getAvatar()
                    )).collect(Collectors.toList())
            );
            posts.add(postContentDetailResponse);
        }
        dto.setContentList(posts);

        OwnerDataResponse owner = converter.OwnerDataResponse(
                dto.getPostOwner() != null ? dto.getPostOwner() : new OwnerDataResponse(),
                appUser.getId(),
                appUser.getName(),
                appUser.getAppUserDetail().getAvatar());
        dto.setPostOwner(owner);


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


}

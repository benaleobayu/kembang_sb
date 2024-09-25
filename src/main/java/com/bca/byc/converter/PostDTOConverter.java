package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import com.bca.byc.repository.*;
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

    @Value("${app.base.url}")
    private final String baseUrl;

    private final ModelMapper modelMapper;

    private final TagRepository tagRepository;
    private final PostLocationRepository postLocationRepository;
    private final PostCategoryRepository postCategoryRepository;


    // for get data
    public PostHomeResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        PostHomeResponse dto = new PostHomeResponse();

        dto.setPostId(data.getId());
        dto.setPostDescription(data.getDescription());

        // set list of post content
        List<PostHomeResponse.PostContent> posts = new ArrayList<>();
        for (PostContent postContent : data.getPostContents()) {
            PostHomeResponse.PostContent post = new PostHomeResponse.PostContent();
            post.setContent(postContent.getContent());
            post.setType(postContent.getType());
            post.setContentId(postContent.getId());
            post.setThumbnail(postContent.getThumbnail());
            posts.add(post);
        }
        dto.setPostContentList(posts);

        List<String> tags = new ArrayList<>();
        for (Tag tag : data.getTags()) {
            tags.add(tag.getName());
        }
        dto.setPostTagsList(tags);

        AppUser appUser = data.getUser();
        UserManagementConverter converter = new UserManagementConverter();
        PostOwnerResponse owner = converter.PostOwnerResponse(
                dto.getPostOwner() != null ? dto.getPostOwner() : new PostOwnerResponse(),
                appUser.getId(),
                appUser.getName(),
                appUser.getAppUserDetail() != null && appUser.getAppUserDetail().getAvatar().startsWith("uploads/") ?
                        baseUrl + "/" + appUser.getAppUserDetail().getAvatar() : null,
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
        // return
        return dto;
    }

    public PostDetailResponse convertToDetailResponse(Post data) {
        // mapping Entity with DTO Entity
        PostDetailResponse dto = modelMapper.map(data, PostDetailResponse.class);
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
        PostLocation postLocation = postLocationRepository.findByPlaceName(dto.getPostLocation().getPlaceName());
        if (postLocation == null) {
            postLocation = new PostLocation();
            postLocation.setPlaceName(dto.getPostLocation().getPlaceName());
            postLocation.setPlaceId(dto.getPostLocation().getPlaceId());
            postLocation.setDescription(dto.getPostLocation().getDescription());
            postLocation.setLatitude(dto.getPostLocation().getLatitude());
            postLocation.setLongitude(dto.getPostLocation().getLongitude());
            postLocation = postLocationRepository.save(postLocation);
        }
        data.setPostLocation(postLocation);

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

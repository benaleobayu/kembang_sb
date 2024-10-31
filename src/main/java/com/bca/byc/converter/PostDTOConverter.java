package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.MaskingBlacklistKeyword;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.*;
import com.bca.byc.repository.AppUserRepository;
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
    private final BusinessCategoryRepository businessCategoryRepository;
    private final LikeDislikeRepository likeDislikeRepository;
    private final AppUserRepository userRepository;
    private final UserHasSavedPostRepository savedPostRepository;

    private final BlacklistKeywordRepository blacklistKeywordRepository;

    @Value("${app.base.url}")
    private String baseUrl;

    public PostHomeResponse convertToDetailResponse(Post data, AppUser userLogin) {
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
        AppUser postUser = data.getUser();

        dto.setPostTagsList(converter.convertTagList(data.getTags()));
        dto.setPostHighlightsList(GlobalConverter.convertListToArray(data.getHighlight()));
        dto.setPostContentList(converter.convertPostContents(data.getPostContents(), converter, userLogin));
        dto.setPostOwner(converter.convertOwnerDataWithBusiness(converter, postUser, userLogin));
        dto.setPostLocation(data.getPostLocation() == null ? null : converter.convertPostLocation(data.getPostLocation()));

        dto.setIsMyPost(data.getUser().getId().equals(userLogin.getId()));

        // Check if the post is liked by the user
        boolean isLiked = likeDislikeRepository.findByPostIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsLiked(isLiked);

        // Check if the user is following the post owner
        boolean isFollowing = userLogin.getFollows().stream().anyMatch(f -> f.getId().equals(data.getUser().getId()));
        dto.setIsFollowed(isFollowing);

        // Check if the user is saved post
        boolean isSaved = savedPostRepository.findByPostIdAndUserId(data.getId(), userLogin.getId()).isPresent();
        dto.setIsSaved(isSaved);

        dto.setIsOfficial(data.getUser().getAppUserAttribute().getIsOfficial());

        dto.setLikeCount(data.getLikesCount());
        dto.setCommentCount(data.getCommentsCount());
        return dto;
    }

    // for create data
    public Post convertToCreateRequest(AppUser user, @Valid PostCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Post data = new Post();
        data.setId(null);

        String sanitizeDescription = MaskingBlacklistKeyword.sanitizeWord(dto.getDescription(), blacklistKeywordRepository);
        data.setDescription(sanitizeDescription);
        data.setUser(user);
        data.setPostAt(dto.getIsPosted().equals(true) ? LocalDateTime.now() : null);

        Set<Tag> tags = new HashSet<>();
        if (dto.getTagName() != null) {
            for (String tagName : dto.getTagName()) {
                boolean containsBlacklistedWord = MaskingBlacklistKeyword.blacklistWords(blacklistKeywordRepository).stream()
                        .anyMatch(blacklistedWord -> tagName.toLowerCase().contains(blacklistedWord.toLowerCase()));

                if (!containsBlacklistedWord) { // Tambahkan hanya jika tidak mengandung blacklist
                    Optional<Tag> tag = tagRepository.findByName(tagName);
                    tag.ifPresentOrElse(tags::add, () -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        tags.add(tagRepository.save(newTag));
                    });
                }
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
        data.setDescription(dto.getDescription());

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

        data.setUpdatedAt(LocalDateTime.now());
    }

    // -----------------


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
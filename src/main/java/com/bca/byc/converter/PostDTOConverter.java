package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.repository.PostLocationRepository;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.util.FileUploadHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostDTOConverter {

    private final ModelMapper modelMapper;

    private final TagRepository tagRepository;
    private final AppUserRepository userRepository;
    private final PostLocationRepository postLocationRepository;


    // for get data
    public PostHomeResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        PostHomeResponse dto = modelMapper.map(data, PostHomeResponse.class);
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
        Post data = modelMapper.map(dto, Post.class);

        data.setId(null);
        // set user
        data.setUser(user);
        // set list of Tags
        Set<Tag> tags = new HashSet<>();
        for (String tagName : dto.getTagName()) {
            Optional<Tag> tag = tagRepository.findByName(tagName);
            tag.ifPresentOrElse(tags::add, () -> {
                Tag newTag = new Tag();
                newTag.setName(tagName);
                newTag = tagRepository.save(newTag);
                tags.add(newTag);
            });
        }
        data.setTags(tags);
        // set list of TagUsers
        Set<AppUser> tagUsers = new HashSet<>();
        for (Long TagUserIds : dto.getTagUserIds()) {
            Optional<AppUser> tagUser = userRepository.findById(TagUserIds);
            tagUser.ifPresent(tagUsers::add);
        }
        data.setTagUsers(tagUsers);

//         set the post location
        PostLocation postLocation = postLocationRepository.findByPlaceName(dto.getPostLocation().getPlaceName());
        if (postLocation == null) {
            postLocation = new PostLocation();
            postLocation.setPlaceName(dto.getPostLocation().getPlaceName());
            postLocation.setPlaceId(dto.getPostLocation().getPlacedId());
            postLocation.setDescription(dto.getPostLocation().getDescription());
            postLocation.setLatitude(dto.getPostLocation().getLatitude());
            postLocation.setLongitude(dto.getPostLocation().getLongitude());
            postLocation = postLocationRepository.save(postLocation);
        }
        data.setPostLocation(postLocation);

        // return
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

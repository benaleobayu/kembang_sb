package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.apps.OwnerDataResponse;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentDTOConverter {

    @Value("${app.base.url}")
    private String baseUrl;

    private ModelMapper modelMapper;

    private final AppUserRepository userRepository;

    // for get data
    public ListCommentResponse convertToPageListResponse(Comment data) {
        // mapping Entity with DTO Entity
        ListCommentResponse dto = modelMapper.map(data, ListCommentResponse.class);
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl);
        OwnerDataResponse owner = converter.OwnerDataResponse(
                new OwnerDataResponse(),
                data.getUser().getSecureId(),
                data.getUser().getName(),
                data.getUser().getAppUserDetail().getAvatar()
        );
        dto.setOwner(owner);
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        // return
        return dto;
    }

    // for get data
    public CommentDetailResponse convertToListResponse(Comment data) {
        // mapping Entity with DTO Entity
        CommentDetailResponse dto = modelMapper.map(data, CommentDetailResponse.class);
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl);
        OwnerDataResponse owner = converter.OwnerDataResponse(
                new OwnerDataResponse(),
                data.getUser().getSecureId(),
                data.getUser().getName(),
                data.getUser().getAppUserDetail().getAvatar()
        );
        dto.setOwner(owner);
        dto.setCreatedAt(Formatter.formatDateTimeApps(data.getCreatedAt()));
        // return
        return dto;
    }


    // for create data
    public Comment convertToCreateRequest(Post postData, @Valid CommentCreateUpdateRequest dto, String email) {
        // mapping DTO Entity with Entity
        Comment data = modelMapper.map(dto, Comment.class);
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found");
        data.setUser(user);
        data.setPost(postData);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Comment data, @Valid CommentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setComment(dto.getComment());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}


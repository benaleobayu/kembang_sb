package com.bca.byc.converter;

import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;
import com.bca.byc.model.apps.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CommentDTOConverter {

    private final AppUserRepository userRepository;
    @Value("${app.base.url}")
    private String baseUrl;
    private ModelMapper modelMapper;

    // for get data
    public ListCommentResponse convertToPageListResponse(Comment data) {
        // mapping Entity with DTO Entity
        int index = 0;
        ListCommentResponse dto = new ListCommentResponse();
        dto.setId(data.getSecureId());
        dto.setIndex(index + 1);
        dto.setComment(data.getComment());
        TreePostConverter converter = new TreePostConverter(baseUrl);
        OwnerDataResponse owner = converter.OwnerDataResponse(
                new OwnerDataResponse(),
                data.getUser().getSecureId(),
                data.getUser().getName(),
                data.getUser().getAppUserDetail().getAvatar()
        );
        dto.setOwner(owner);
        ListCommentReplyResponse commentReplyResponse = converter.convertToListCommentReplyResponse(
                new ListCommentReplyResponse(),
                data.getSecureId(),
                index + 1,
                data.getComment(),
                converter.OwnerDataResponse(
                        new OwnerDataResponse(),
                        data.getUser().getSecureId(),
                        data.getUser().getName(),
                        data.getUser().getAppUserDetail().getAvatar()
                ),
                Formatter.formatDateTimeApps(data.getCreatedAt())
        );
        List<ListCommentReplyResponse> commentReplyResponseList = new ArrayList<>();
        commentReplyResponseList.add(commentReplyResponse);
        dto.setCommentReply(commentReplyResponseList);
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
    public CommentReply convertToCreateReplyRequest(Post postData, CommentCreateUpdateRequest dto, String id) {
        // mapping DTO Entity with Entity
        CommentReply data = new CommentReply();
        // return
        return data;
    }

    public void convertToUpdateRequest(Comment data, @Valid CommentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setComment(dto.getComment());
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}


package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.entity.Tag;
import com.bca.byc.model.AdminContentCreateUpdateRequest;
import com.bca.byc.model.AdminContentDetailResponse;

import com.bca.byc.model.AdminContentIndexResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AdminContentDTOConverter {

    private ModelMapper modelMapper;

    // for get data index
    public AdminContentIndexResponse<Long> convertToIndexResponse(Post data, String baseUrl) {
        // mapping Entity with DTO Entity
        PostContent firstPostContent = data.getPostContents().getFirst();
        String thumbnail;
        if (firstPostContent != null && firstPostContent.getType().equals("image")) {
            thumbnail = firstPostContent.getContent();
        } else {
            assert firstPostContent != null;
            thumbnail = firstPostContent.getThumbnail();
        }

        List<String> tags = data.getTags().stream().map(Tag::getName).toList();

        // return
        return new AdminContentIndexResponse<>(
                data.getSecureId(), // id
                data.getId(), // index
                GlobalConverter.convertListToArray(data.getHighlight()), // get highlight
                GlobalConverter.getParseImage(thumbnail, baseUrl), // get thumbnail
                data.getDescription(),
                GlobalConverter.convertListToArray(String.join(",", tags)),
                data.getAdmin().getName(),
                data.getIsActive(),
                data.getPromotedStatus(),
                data.getPromotedAt() + "-" + data.getPromotedUntil()
        );
    }
  // for get data
    public AdminContentDetailResponse convertToDetailResponse(Post data, String baseUrl) {
        // mapping Entity with DTO Entity
        PostContent firstPostContent = data.getPostContents().getFirst();
        String thumbnail;
        if (firstPostContent != null && firstPostContent.getType().equals("image")) {
            thumbnail = firstPostContent.getContent();
        } else {
            assert firstPostContent != null;
            thumbnail = firstPostContent.getThumbnail();
        }

        List<String> tags = data.getTags().stream().map(Tag::getName).toList();

        // return
        return new AdminContentDetailResponse(
                data.getSecureId(), // id
                GlobalConverter.convertListToArray(data.getHighlight()), // get highlight
                GlobalConverter.getParseImage(thumbnail, baseUrl), // get thumbnail
                data.getDescription(),
                GlobalConverter.convertListToArray(String.join(",", tags)),
                data.getAdmin().getName(),
                data.getIsActive(),
                data.getPromotedStatus(),
                data.getPromotedAt() + "-" + data.getPromotedUntil()
        );
    }

    // for create data
    public Post convertToCreateRequest(@Valid AdminContentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Post data = modelMapper.map(dto, Post.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Post data, @Valid AdminContentCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}


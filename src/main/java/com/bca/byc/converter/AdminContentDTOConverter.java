package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.entity.Tag;
import com.bca.byc.model.AdminContentCreateUpdateRequest;
import com.bca.byc.model.AdminContentDetailResponse;

import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.model.ChannelChecklistResponse;
import com.bca.byc.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                data.getSecureId() == null ? null : data.getSecureId(), // id
                data.getId() == null ? null : data.getId(), // index
                data.getHighlight() == null ? null : GlobalConverter.convertListToArray(data.getHighlight()), // get highlight
                thumbnail == null ? null : GlobalConverter.getParseImage(thumbnail, baseUrl), // get thumbnail
                data.getDescription() == null ? null : data.getDescription(),
                tags == null ? null : GlobalConverter.convertListToArray(String.join(",", tags)),
                data.getUpdatedBy() == null ? null : data.getUpdatedBy().getEmail(),
                data.getIsActive() == null ? null : data.getIsActive(),
                data.getPromotedStatus() == null ? null : data.getPromotedStatus(),
                data.getPromotedAt() == null || data.getPromotedUntil() == null ? null : data.getPromotedAt() + "-" + data.getPromotedUntil(),
                data.getCreatedBy() == null ? null : data.getCreatedBy().getEmail(),
                data.getCreatedAt() == null ? null : Formatter.formatLocalDateTime(data.getCreatedAt())
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

        ChannelChecklistResponse channel = new ChannelChecklistResponse();
        channel.setId(data.getChannel().getSecureId());
        channel.setName(data.getChannel().getName());
        channel.setIsChecked(true);

        List<String> contents = data.getPostContents().stream().map( d -> GlobalConverter.getParseImage(d.getContent(), baseUrl)).collect(Collectors.toList());

        // return
        return new AdminContentDetailResponse(
                data.getSecureId(), // id
                channel,
                GlobalConverter.convertListToArray(data.getHighlight()), // get highlight
                GlobalConverter.getParseImage(thumbnail, baseUrl), // get thumbnail
                contents,
                data.getContentType(),
                data.getDescription(),
                GlobalConverter.convertListToArray(String.join(",", tags)),
                data.getAdmin().getName(),
                data.getIsActive(),
                data.getIsActive(),
                data.getPromotedStatus(),
                data.getPromotedAt() + "-" + data.getPromotedUntil(),
                Formatter.formatLocalDateTime(data.getUpdatedAt())
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


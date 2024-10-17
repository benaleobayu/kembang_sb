package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Post;
import com.bca.byc.model.ChanelDetailResponse;
import com.bca.byc.model.ChanelIndexResponse;
import com.bca.byc.model.ChanelListContentResponse;
import com.bca.byc.model.ChannelCreateUpdateRequest;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ChanelDTOConverter {

    private ModelMapper modelMapper;

    // for get data index
    public ChanelIndexResponse convertToIndexResponse(Channel data, String baseUrl) {
        // mapping Entity with DTO Entity
        return new ChanelIndexResponse(
                data.getSecureId(),
                data.getName(),
                data.getOrders(),
                data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null,
                data.getDescription(),
                GlobalConverter.getParseImage(data.getLogo(), baseUrl),
                data.getPrivacy()
        );
    }

    // for get data
    public ChanelDetailResponse convertToDetailResponse(Channel data, String baseUrl) {
        // mapping Entity with DTO Entity
        // return
        return new ChanelDetailResponse(
                data.getSecureId(),
                data.getName(),
                data.getOrders(),
                data.getUpdatedAt() != null ? Formatter.formatLocalDateTime(data.getUpdatedAt()) : null,
                data.getDescription(),
                GlobalConverter.getParseImage(data.getLogo(), baseUrl),
                data.getPrivacy()
        );
    }

    // for create data
    public Channel convertToCreateRequest(@Valid ChannelCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Channel data = new Channel();
        data.setName(dto.name());
        data.setDescription(dto.description());
        data.setOrders(dto.orders());
        data.setPrivacy(dto.privacy());
        data.setIsActive(dto.status());
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Channel data, @Valid ChannelCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        data.setName(dto.name());
        data.setDescription(dto.description());
        data.setOrders(dto.orders());
        data.setPrivacy(dto.privacy());
        data.setIsActive(dto.status());

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

    public ChanelListContentResponse<Long> convertListContentResponse(Post data, String baseUrl) {

        String firstTypeContent = data.getPostContents().getFirst().getType();
        String firstContentThumbnail;
        if (firstTypeContent != null && firstTypeContent.equals("image")) {
            firstContentThumbnail = GlobalConverter.getParseImage(data.getPostContents().getFirst().getContent(), baseUrl);
        } else {
            firstContentThumbnail = GlobalConverter.getParseImage(data.getPostContents().getFirst().getThumbnail(), baseUrl);
        }

        AppAdmin admin = data.getAdmin();

        return new ChanelListContentResponse<Long>(
                data.getSecureId(),
                data.getId(),
                firstContentThumbnail,
                data.getDescription(),
                admin.getName(),
                GlobalConverter.getAvatarImage(admin.getAvatar(), baseUrl),
                admin.getAccountType() != null ? admin.getAccountType() : null,
                data.getLikesCount(),
                data.getCommentsCount(),
                data.getSharesCount()
        );
    }
}


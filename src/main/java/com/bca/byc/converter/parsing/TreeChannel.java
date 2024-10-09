package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Channel;
import com.bca.byc.model.ChannelCreateUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class TreeChannel {

    public static Channel savedChannel(
            String name,
            Integer orders,
            MultipartFile logo,
            String description,
            String privacy,
            Boolean status
    ){
        Channel newChannel = new Channel();
        newChannel.setName(name);
        newChannel.setOrders(orders);
        newChannel.setDescription(description);
        newChannel.setPrivacy(privacy);
        newChannel.setIsActive(status);
        return newChannel;
    }

    public static void updatedChannel(
            ChannelCreateUpdateRequest dto,
            Channel data,
            AppAdmin admin
    ){
        data.setName(dto.name());
        data.setOrders(dto.orders());
        data.setDescription(dto.description());
        data.setPrivacy(dto.privacy());
        data.setIsActive(dto.status());
        data.setCreatedBy(admin);
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
    }

    public static ChannelCreateUpdateRequest ChannelPartDTO(
            String name,
            Integer orders,
            MultipartFile logo,
            String description,
            String privacy,
            Boolean status
    ){
        return new ChannelCreateUpdateRequest(
                name,
                orders,
                logo,
                description,
                status,
                privacy
        );
    }

}

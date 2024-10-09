package com.bca.byc.model;

import org.springframework.web.multipart.MultipartFile;

public record ChannelCreateUpdateRequest(

        String name,

        Integer orders,

        MultipartFile logo,

        String description,

        Boolean status,

        String privacy
) {
}

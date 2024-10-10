package com.bca.byc.model;

public record ChanelListContentResponse<S>(

        String id,

        S index,

        String thumbnail,

        String description,

        String ownerName,

        String ownerAvatar,

        String ownerType,

        Long totalLike,

        Long totalComment,

        Long totalShare

) {
}

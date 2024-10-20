package com.bca.byc.model;

public record ChanelDetailResponse(

        String id,

        String name,

        Integer orders,

        String updatedAt,

        String description,

        String logo,

        String privacy,

        Boolean status

) {
}

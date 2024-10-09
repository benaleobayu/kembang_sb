package com.bca.byc.model;

public record ChanelCreateUpdateRequest(

        String name,

        String orders,

        String logo,

        String description,

        Boolean status,

        String privacy
) {
}

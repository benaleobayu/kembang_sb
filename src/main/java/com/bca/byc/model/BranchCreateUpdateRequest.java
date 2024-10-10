package com.bca.byc.model;

public record BranchCreateUpdateRequest(
        String code,

        String name,

        String address,

        String phone,

        Boolean status,

        String locationId,

        String kanwilId

) {
}

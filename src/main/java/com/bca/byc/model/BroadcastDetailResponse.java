package com.bca.byc.model;

public record BroadcastDetailResponse(

        String title,

        String message,

        Boolean status,

        String postAt
) { }
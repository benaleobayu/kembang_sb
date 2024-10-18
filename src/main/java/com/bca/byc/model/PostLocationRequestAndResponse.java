package com.bca.byc.model;

import lombok.Data;

@Data
public class PostLocationRequestAndResponse {

    private String placeName;

    private String description;

    private String placeId;

    private Double longitude;

    private Double latitude;
}

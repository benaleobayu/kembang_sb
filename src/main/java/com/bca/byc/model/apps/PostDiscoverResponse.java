package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class PostDiscoverResponse {

    private String channelId;

    private String channelName;

    private Boolean isSeeMore = false;

    private List<ListPostDiscoverResponse> categories;

}

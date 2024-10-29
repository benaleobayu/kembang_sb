package com.bca.byc.model.apps;

import lombok.Data;

import java.util.List;

@Data
public class PostDiscoverResponse {

    private List<PostOnChannelResponse> categories;

}

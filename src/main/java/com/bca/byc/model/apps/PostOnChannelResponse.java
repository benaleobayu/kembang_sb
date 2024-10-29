package com.bca.byc.model.apps;

import com.bca.byc.model.PostHomeResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostOnChannelResponse {

    private String categoryId;
    private String categoryName;
    private Boolean isSeeMore;

    private List<PostHomeResponse> post;

}

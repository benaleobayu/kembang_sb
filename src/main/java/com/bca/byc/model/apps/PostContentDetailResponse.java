package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostContentDetailResponse {

    private String contentId;
    private String content;
    private String contentType;
    private String thumbnail;

    private List<OwnerDataResponse> contentTagsUser = new ArrayList<>();

}

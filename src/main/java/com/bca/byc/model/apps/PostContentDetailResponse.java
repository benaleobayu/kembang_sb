package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostContentDetailResponse {

    private Long contentId;
    private String content;
    private String contentType;
    private String thumbnail;

    private List<TagUserResponse> contentTagsUser = new ArrayList<>();

}

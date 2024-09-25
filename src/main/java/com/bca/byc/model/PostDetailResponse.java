package com.bca.byc.model;

import com.bca.byc.model.apps.PostContentDetailResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailResponse {

    private Long id;
    private String description;
    private Boolean status;

    private List<PostContentDetailResponse> contentList; //

}

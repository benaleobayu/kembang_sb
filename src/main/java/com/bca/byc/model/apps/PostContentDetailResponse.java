package com.bca.byc.model.apps;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostContentDetailResponse {

    private Long id;
    private String content;
    private String type;
    private String thumbnail;

    private List<TagUserResponse> tagUser = new ArrayList<>();

}

package com.bca.byc.model;

import lombok.Data;

@Data
public class PostCreateUpdateRequest {

    private String name;

    private String title;

    private String description;

    private String content;

    private String type;
}

package com.bca.byc.model.attribute;

import lombok.Data;

import java.util.List;

@Data
public class PostContentRequest {

    private Integer index;

    private String content;

    private String type;

    private List<Long> tagUserIds; // user _id

    private String originalName;

}

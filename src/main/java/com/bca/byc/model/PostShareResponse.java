package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class PostShareResponse {

    private String postId;
    private List<String> sharedUserIds;

}

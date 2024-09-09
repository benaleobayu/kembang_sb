package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class AppSearchDetailResponse {

    private Long id;

    private String title;

    private String description;

    private String content;

    private String type;

    private List<TagResponse> tags;

    private AppUserResponse appUser;

    @Data
    public static class AppUserResponse {
        private String name;
    }

    @Data
    public static class TagResponse {
        private String name;
    }

}

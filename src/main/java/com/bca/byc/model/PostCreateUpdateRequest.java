package com.bca.byc.model;

import com.bca.byc.entity.PostLocation;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    private String title;

    private String description;

    private List<String> tagName;

    // can input multiple tags
    @Transient
    private List<Long> tagIds; // <tag_id>

    // can input multiple users
    private List<Long> tagUserIds; // <user_id>

    private PostLocationRequest postLocation;

    @Transient
    private String content;

    @Transient
    private String type;

    @Data
    public static class PostLocationRequest {

        private String name;

        private String url;

        private String geoLocation;
    }
}

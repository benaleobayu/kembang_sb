package com.bca.byc.model;

import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateUpdateRequest {

    private String title;

    private String description;

    // can input multiple tags
    private List<Long> tags; // <tag_id>

    // can input multiple users
    private List<Long> tagUsers; // <user_id>

    private Long locationId;

    @Transient
    private String content;

    @Transient
    private String type;
}

package com.bca.byc.model;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class PostDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String name;
    private String content;
    private String type;
    private Boolean status;

}

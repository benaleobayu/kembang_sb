package com.bca.byc.model.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PostContentRequest {

    @Schema(description = "let this be null", example = "")
    private Integer index;

    @Schema(description = "let this be null", example = "")
    private String content;

    @Schema(description = "let this be null", example = "")
    private String type;

    @Schema(description = "tag ids person", example = "1,2,3,4")
    private List<Long> tagUserIds; // user _id

    @Schema(description = "fill this for the original name", example = "abc.jpg")
    private String originalName;

    @Schema(description = "for generate thumbnail from video", example = "abc.jpg")
    private String thumbnail;

}

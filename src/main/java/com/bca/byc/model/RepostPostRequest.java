package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RepostPostRequest {

    @Schema(example = "post id")
    private String postId;

    @Schema(example = "This content is not appropriate")
    private String reason;

}

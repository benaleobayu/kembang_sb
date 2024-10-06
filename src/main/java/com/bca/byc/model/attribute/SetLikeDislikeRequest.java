package com.bca.byc.model.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SetLikeDislikeRequest {

    @Schema(example = "POST | COMMENT | COMMENT_REPLY")
    private String type;

    private String targetId;

}

package com.bca.byc.model.returns;

import com.bca.byc.model.apps.CommentDetailResponse;
import lombok.Data;

@Data
public class ReturnCommentResponse {

    private CommentDetailResponse commentDetail;

    private Integer total = 0;

    public ReturnCommentResponse() {
    }

    public ReturnCommentResponse(Integer total) {
        if (total != null) {
            this.total = total;
        }
    }

}

package com.bca.byc.model.returns;

import com.bca.byc.model.apps.CommentDetailResponse;
import lombok.Data;

@Data
public class ReturnCommentResponse {

    private CommentDetailResponse commentDetail;

    private Integer totalComments = 0;

    private Integer totalReplies = 0;

    public ReturnCommentResponse() {
    }

    public ReturnCommentResponse(Integer totalComments, Integer totalReplies) {
        if (totalComments != null) {
            this.totalComments = totalComments;
        }
         if (totalReplies != null) {
            this.totalReplies = totalReplies;
        }
    }

}
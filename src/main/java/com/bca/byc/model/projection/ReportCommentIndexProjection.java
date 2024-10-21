package com.bca.byc.model.projection;

import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportCommentIndexProjection {

    private String id;
    private Long index;
    private Long reportId;
    private String comment;
    private String statusReport;

}

package com.bca.byc.model.projection;

import java.time.LocalDateTime;

public interface ReportCommentIndexProjection {

    String getId(); // get report secureId

    Long getIndex(); // get report id

    String getThumbnail(); // get thumbnail while report -> comment -> post -> has many post contents and if post content type is video -> get thumbnail but if not video -> get content

    String getComment(); // get comment while report -> comment_id is null get comment of commentReply.comment else get comment.comment

    String getCommentOwner(); // get comment owner while report -> comment_id is null get comment of commentReply.commentOwner else get comment.commentOwner

    String getStatusReport(); // get report status

    Integer getTotalReports(); // get total report while report-> comment_id is null get comment of commentReply.comment else get comment.comment

    LocalDateTime getLastReport(); // get last report on report.createdAt of last

}

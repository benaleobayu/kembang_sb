package com.bca.byc.service;

import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.attribute.TotalCountResponse;

public interface CommentServiceReply {

    TotalCountResponse saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String commentId);

    void updateDataCommentReply(String postId, String parentCommentId, CommentCreateUpdateRequest dto, String id);

    void deleteDataCommentReply(String postId, String commentId, String id);
}

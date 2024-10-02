package com.bca.byc.service;

import com.bca.byc.model.apps.CommentCreateUpdateRequest;

public interface CommentServiceReply {

    void saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String commentId);

    void updateDataCommentReply(String postId, String parentCommentId, CommentCreateUpdateRequest dto, String id);

    void deleteDataCommentReply(String postId, String commentId, String id);
}

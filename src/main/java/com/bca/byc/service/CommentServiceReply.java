package com.bca.byc.service;

import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.attribute.TotalCountResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface CommentServiceReply {

    ResultPageResponseDTO<ListCommentReplyResponse> listDataCommentReplies(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId, String parentCommentId);

    TotalCountResponse saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String commentId);

    void updateDataCommentReply(String postId, String parentCommentId, CommentCreateUpdateRequest dto, String id);

    void deleteDataCommentReply(String postId, String commentId, String id);
}

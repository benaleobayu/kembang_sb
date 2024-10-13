package com.bca.byc.service;

import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.ListCommentReplyResponse;
import com.bca.byc.model.returns.ReturnCommentResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface CommentReplyService {

    ResultPageResponseDTO<ListCommentReplyResponse> listDataCommentReplies(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId, String parentCommentId);

    ReturnCommentResponse saveDataCommentReply(String postId, CommentCreateUpdateRequest dto, String commentId);

    void updateDataCommentReply(String postId, String parentCommentId, CommentCreateUpdateRequest dto, String id);

    void deleteDataCommentReply(String postId, String commentId, String id);
}

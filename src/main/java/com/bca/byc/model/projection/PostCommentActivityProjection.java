package com.bca.byc.model.projection;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.entity.Post;

public interface PostCommentActivityProjection {

    Comment getComment();

    CommentReply getCommentReply();

    AppUser getCommentUser();

    AppUser getReplyUser();

    Post getPost();
}

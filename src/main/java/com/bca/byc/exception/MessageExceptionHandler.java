package com.bca.byc.exception;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;

public class MessageExceptionHandler {

    public static void checkCommentOwnership(Comment comment, Post post) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Failed delete comment, comment is not from correct post");
        }
    }

    public static void checkCommentUser(Comment comment, AppUser user) {
        if (!comment.getPost().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Failed delete comment, comment is not from correct user");
        }
    }

}

package com.bca.byc.exception;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;

public class MessageExceptionHandler {

    public static void checkCommentOnPost(Comment comment, Post post, String message) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Failed " + message + " comment, comment is not from correct post");
        }
    }

    public static void checkCommentUser(Comment comment, AppUser user, String message) {
        if (!comment.getPost().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Failed " + message + " comment, comment is not from correct user");
        }
    }

}

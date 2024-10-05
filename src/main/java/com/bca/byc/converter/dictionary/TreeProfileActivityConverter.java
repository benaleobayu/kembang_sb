package com.bca.byc.converter.dictionary;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.CommentReply;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.apps.SimplePostResponse;
import com.bca.byc.util.helper.Formatter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

import static com.bca.byc.converter.parsing.GlobalConverter.getAvatarUser;

public class TreeProfileActivityConverter {

    private SimplePostResponse convertPostData(
            SimplePostResponse dto,
            AppUser user,
            String baseUrl
    ) {
        dto.setPostId(user.getPosts().getFirst().getSecureId());
        dto.setPostDescription(user.getPosts().getFirst().getDescription());
        dto.setPostContent(GlobalConverter.getAvatarUser(user.getPosts().getFirst().getPostContents().getFirst().getContent(), baseUrl));
        dto.setPostCreatedAt(user.getPosts().getFirst().getCreatedAt() != null ? Formatter.formatDateTimeApps(user.getPosts().getFirst().getCreatedAt()) : null);
        return dto;
    }

    public ProfileActivityPostCommentsResponse convertActivityComments(
            ProfileActivityPostCommentsResponse dto,
            AppUser user,
            Comment comment,
            SimplePostResponse postDto,
            String baseUrl

    ) {
        dto.setUserId(user.getSecureId());
        dto.setUserName(user.getAppUserDetail().getName());
        dto.setUserAvatar(getAvatarUser(user.getAppUserDetail().getAvatar(), baseUrl));
        dto.setPost(convertPostData(postDto, user, baseUrl));
        TreePostConverter converter = new TreePostConverter(baseUrl);
        dto.setComments(Collections.singletonList(converter.convertToListCommentResponse(
                new ListCommentResponse(),
                comment.getSecureId(),
                comment.getId(),
                comment.getComment(),
                comment.getCommentReply(),
                comment.getUser(),
                comment.getCreatedAt()
        )));
        return dto;
    }

}
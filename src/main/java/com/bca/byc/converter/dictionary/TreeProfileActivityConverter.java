package com.bca.byc.converter.dictionary;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreePostConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.model.PostHomeResponse;
import com.bca.byc.model.apps.ListCommentResponse;
import com.bca.byc.model.apps.ProfileActivityPostCommentsResponse;
import com.bca.byc.model.apps.SimplePostResponse;
import com.bca.byc.repository.LikeDislikeRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.util.helper.Formatter;

import java.util.Collections;

import static com.bca.byc.converter.parsing.GlobalConverter.getParseImage;

public class TreeProfileActivityConverter {

    private SimplePostResponse convertPostData(
            SimplePostResponse dto,
            AppUser user,
            String baseUrl
    ) {
        PostContent firstPostContent = user.getPosts().getFirst().getPostContents().getFirst();

        dto.setPostId(user.getPosts().getFirst().getSecureId());
        dto.setPostDescription(user.getPosts().getFirst().getDescription());
        dto.setPostContent(GlobalConverter.getParseImage(firstPostContent.getContent(), baseUrl));
        dto.setPostThumbnail(GlobalConverter.getParseImage(firstPostContent.getThumbnail(), baseUrl));
        dto.setPostCreatedAt(user.getPosts().getFirst().getCreatedAt() != null ? Formatter.formatDateTimeApps(user.getPosts().getFirst().getCreatedAt()) : null);
        return dto;
    }

    public ProfileActivityPostCommentsResponse convertActivityComments(
            ProfileActivityPostCommentsResponse dto,
            AppUser user,
            AppUser userLogin,
            Post post,
            Comment comment,
            String baseUrl,
            LikeDislikeRepository likeDislikeRepository,
            AppUserRepository userRepository

    ) {
        TreePostConverter converter = new TreePostConverter(baseUrl, userRepository);
        dto.setUserId(user.getSecureId());
        dto.setUserName(user.getAppUserDetail().getName());
        dto.setUserAvatar(getParseImage(user.getAppUserDetail().getAvatar(), baseUrl));
        dto.setPost(converter.convertToPostHomeResponse(
                new PostHomeResponse(),
                post,
                converter,
                userLogin,
                likeDislikeRepository
        ));
        dto.setComments(Collections.singletonList(converter.convertToListCommentResponse(
                new ListCommentResponse(),
                user,
                comment,
                likeDislikeRepository
        )));
        return dto;
    }

}

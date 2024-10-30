package com.bca.byc.service;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.*;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.NotificationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {

    private final AppUserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    @Value("${app.base.url}")
    private String baseUrl;

    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    public ResultPageResponseDTO<NotificationResponse> getNotificationsByUserId(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<Notification> pageResult = notificationRepository.findByUserIdAndKeyword(userLogin.getId(), set.keyword(), set.pageable());
        List<NotificationResponse> dtos = pageResult.stream().map((data) -> {

            String postId = null;
            String postThumbnail = null;
            String messages;
            switch (data.getNotifiableType()){
                case "LIKE-POST":
                    Post post = HandlerRepository.getEntityBySecureId(data.getNotifiableId(), postRepository, "Post not found");
                    postId = data.getNotifiableId();
                    postThumbnail = getPostThumbnail(post);
                    messages = "Liked your post";
                    break;
                case "LIKE-COMMENT":
                    Comment comment = HandlerRepository.getEntityBySecureId(data.getNotifiableId(), commentRepository, "Comment not found");
                    postId = comment.getPost().getSecureId();
                    postThumbnail = getPostThumbnail(comment.getPost());
                    messages = "Liked your comment";
                    break;
                case "LIKE-COMMENT-REPLY":
                    CommentReply reply = HandlerRepository.getEntityBySecureId(data.getNotifiableId(), commentReplyRepository, "Comment not found");
                    postId = reply.getParentComment().getPost().getSecureId();
                    postThumbnail = getPostThumbnail(reply.getParentComment().getPost());
                    messages = "Liked your comment reply";
                    break;
                case "FOLLOWERS":
                    messages = "Starting following you";
                    break;
                case "COMMENT":
                    Comment cComment = HandlerRepository.getEntityBySecureId(data.getNotifiableId(), commentRepository, "Comment not found");
                    postThumbnail = getPostThumbnail(cComment.getPost());
                    messages = data.getMessage();
                    break;
                case "COMMENT-REPLY":
                    CommentReply cReply = HandlerRepository.getEntityBySecureId(data.getNotifiableId(), commentReplyRepository, "Comment not found");
                    postThumbnail = getPostThumbnail(cReply.getParentComment().getPost());
                    messages = data.getMessage();
                    break;
                default:
                    messages = "Error";
                    break;
            }

            String status;
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfWeek = today.plusDays(6);
            if (data.getCreatedAt().toLocalDate().equals(LocalDate.now())) {
                status = "Recent";
            } else if (data.getCreatedAt().isAfter(today) && data.getCreatedAt().isBefore(endOfWeek)) {
                status = "This week";
            } else {
                status = "Other";
            }

            List<AppUser> followers = userRepository.findFollowersByUserId(data.getId());
            boolean isFollowed = followers.stream().anyMatch(f -> f.getId().equals(userLogin.getId()));

            AppUser userFrom = HandlerRepository.getEntityById(data.getCreatedBy(), userRepository, "User not found");
            return new NotificationResponse(
                    data.getSecureId(),
                    data.getNotifiableType(),
                    data.getReadAt() == null ? null : data.getReadAt().format(DateTimeFormatter.ISO_DATE_TIME),
                    data.getCreatedAt() == null ? null : data.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                    messages,
                    userFrom.getSecureId(),
                    GlobalConverter.getAvatarImage(userFrom.getAppUserDetail().getAvatar(), baseUrl),
                    userFrom.getAppUserDetail().getName(),
                    isFollowed,
                    postId,
                    GlobalConverter.getParseImage(postThumbnail, baseUrl),
                    data.getIsRead(),
                    status
            );
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    // --helper
    private String getPostThumbnail(Post post){
        return post.getContentType().equals("video") ?
                post.getPostContents().stream().filter( f -> f.getType().equals("video")).map(PostContent::getThumbnail).findFirst().orElse(null) :
                post.getPostContents().stream().filter( f -> f.getType().equals("image")).map(PostContent::getThumbnail).findFirst().orElse(null);
    }
}

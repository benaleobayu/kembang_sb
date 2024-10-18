package com.bca.byc.seeder;

import com.bca.byc.entity.*;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReportPostJob {

    private final AppUserRepository userRepository;

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository replyRepository;

    //    @Scheduled(fixedDelay = 50)
    public void saveDataInDb() {
        Faker faker = new Faker();
        String[] postStatus = {"DRAFT", "REVIEW", "REJECT", "TAKE_DOWN"};
        Long totalPost = postRepository.count();
        Integer randomPost = faker.random().nextInt(1, totalPost.intValue());
        Post post = postRepository.findById(Long.valueOf(randomPost)).orElse(null);

        Report newReportPost = new Report();
        newReportPost.setPost(post);
        newReportPost.setType("POST");
        newReportPost.setReason(faker.lorem().sentence());
        newReportPost.setStatus(postStatus[faker.number().numberBetween(0, 4)]);
        newReportPost.setCreatedAt(LocalDateTime.now());
        newReportPost.setReporterUser(userRepository.findById(189L).orElse(null));
        reportRepository.save(newReportPost);

    }

//    @Scheduled(fixedDelay = 50)
    public void saveDataReportComment() {
        Faker faker = new Faker();
        String[] postStatus = {"DRAFT", "REVIEW", "REJECT", "TAKE_DOWN"};
        Long totalPost = commentRepository.count();
        Integer random = faker.random().nextInt(1, totalPost.intValue());
        Comment data = commentRepository.findById(Long.valueOf(random)).orElse(null);

        Report newReportPost = new Report();
        newReportPost.setComment(data);
        newReportPost.setType("COMMENT");
        newReportPost.setReason(faker.lorem().sentence());
        newReportPost.setStatus(postStatus[faker.number().numberBetween(0, 4)]);
        newReportPost.setCreatedAt(LocalDateTime.now());
        newReportPost.setReporterUser(userRepository.findById(189L).orElse(null));
        reportRepository.save(newReportPost);

    }

//    @Scheduled(fixedDelay = 50)
    public void saveDataReportCommentReply() {
        Faker faker = new Faker();
        String[] postStatus = {"DRAFT", "REVIEW", "REJECT", "TAKE_DOWN"};
        Long totalPost = replyRepository.count();
        Integer random = faker.random().nextInt(1, totalPost.intValue());
        CommentReply data = replyRepository.findById(Long.valueOf(random)).orElse(null);

        Report newReportPost = new Report();
        newReportPost.setCommentReply(data);
        newReportPost.setType("COMMENT_REPLY");
        newReportPost.setReason(faker.lorem().sentence());
        newReportPost.setStatus(postStatus[faker.number().numberBetween(0, 4)]);
        newReportPost.setCreatedAt(LocalDateTime.now());
        newReportPost.setReporterUser(userRepository.findById(189L).orElse(null));
        reportRepository.save(newReportPost);

    }

//    @Scheduled(fixedDelay = 50)
    public void saveDataReportUser() {
        Faker faker = new Faker();
        String[] postStatus = {"DRAFT", "REVIEW", "REJECT", "TAKE_DOWN"};
        Long totalPost = userRepository.count();
        Integer random = faker.random().nextInt(1, totalPost.intValue());
        AppUser data = userRepository.findById(Long.valueOf(random)).orElse(null);

        Report newReportPost = new Report();
        newReportPost.setReportedUser(data);
        newReportPost.setType("USER");
        newReportPost.setReason(faker.lorem().sentence());
        newReportPost.setStatus(postStatus[faker.number().numberBetween(0, 4)]);
        newReportPost.setCreatedAt(LocalDateTime.now());
        newReportPost.setReporterUser(userRepository.findById(189L).orElse(null));
        reportRepository.save(newReportPost);

    }


}

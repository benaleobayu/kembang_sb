package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.model.ReportRequest;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.RepostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;

@Service
@AllArgsConstructor
public class RepostServiceImpl implements RepostService {

    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    private final ReportRepository reportRepository;

    @Override
    public void SendReport (ReportRequest dto) throws Exception {
        AppUser UserId = GlobalConverter.getUserEntity(appUserRepository);

        Report report = new Report();
        report.setType(dto.getType());
        report.setReporterUser(UserId);
        report.setReport(dto.getReason());

        switch (dto.getType()) {
            case "POST":
                report.setPost(getEntityBySecureId(dto.getReportedId(), postRepository, "Post not found"));
                break;
            case "COMMENT":
                report.setComment(getEntityBySecureId(dto.getReportedId(), commentRepository, "Comment not found"));
                break;
            case "COMMENT_REPLY":
                report.setCommentReply(getEntityBySecureId(dto.getReportedId(), commentReplyRepository, "Comment not found"));
                break;
            case "USER":
                report.setReportedUser(getEntityBySecureId(dto.getReportedId(), appUserRepository, "User not found"));
                break;
        }

        reportRepository.save(report);
    }
}

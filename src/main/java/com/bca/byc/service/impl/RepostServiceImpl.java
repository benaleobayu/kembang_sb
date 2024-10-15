package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
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
    public String SendReport(ReportRequest dto) throws Exception {
        AppUser userId = GlobalConverter.getUserEntity(appUserRepository);

        Report report = new Report();
        report.setType(dto.getType());
        report.setReporterUser(userId);
        report.setReason(dto.getReason());
        report.setStatus("DRAFT");

        String message;
        switch (dto.getType()) {
            case "POST":
                report.setPost(getEntityBySecureId(dto.getReportedId(), postRepository, "Post not found"));
                message = "Post reported successfully";
                break;
            case "COMMENT":
                report.setComment(getEntityBySecureId(dto.getReportedId(), commentRepository, "Comment not found"));
                message = "Comment reported successfully";
                break;
            case "COMMENT_REPLY":
                report.setCommentReply(getEntityBySecureId(dto.getReportedId(), commentReplyRepository, "Comment not found"));
                message = "Comment reply reported successfully";
                break;
            case "USER":
                report.setReportedUser(getEntityBySecureId(dto.getReportedId(), appUserRepository, "User not found"));
                message = "User reported successfully";
                break;
            default:
                throw new BadRequestException("Invalid type");
        }
        reportRepository.save(report);

        return message;
    }
}

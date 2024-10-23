package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ChangeStatusRequest;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.ReportListDetailResponse;
import com.bca.byc.model.ReportRequest;
import com.bca.byc.model.projection.ReportListDetailProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.CommentReplyRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportService;
import com.bca.byc.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.repository.handler.HandlerRepository.getEntityBySecureId;
import static com.bca.byc.repository.handler.HandlerRepository.getIdBySecureId;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final AppAdminRepository adminRepository;
    private final AppUserRepository appUserRepository;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    @Override
    public String SendReport(ReportRequest dto) throws Exception {
        AppUser userId = GlobalConverter.getUserEntity(appUserRepository);

        Report report = new Report();
        report.setType(dto.getType());
        report.setReporterUser(userId);
        report.setReason(dto.getReason());
        report.setOtherReason(dto.getOtherReason());
        report.setStatus("DRAFT");

        String message;
        switch (dto.getType()) {
            case "POST":
                Post post = getEntityBySecureId(dto.getReportedId(), postRepository, "Post not found");
                report.setPost(post);
                post.setReportStatus("REVIEW");
                postRepository.save(post);
                message = "Post reported successfully";

                break;
            case "COMMENT":
                Comment comment = getEntityBySecureId(dto.getReportedId(), commentRepository, "Comment not found");
                report.setComment(comment);
                message = "Comment reported successfully";
                comment.setReportStatus("REVIEW");
                commentRepository.save(comment);
                break;
            case "COMMENT_REPLY":
                CommentReply commentReply = getEntityBySecureId(dto.getReportedId(), commentReplyRepository, "Comment not found");
                report.setCommentReply(commentReply);
                message = "Comment reply reported successfully";
                commentReply.setReportStatus("REVIEW");
                commentReplyRepository.save(commentReply);
                break;
            case "USER":
                AppUser user = getEntityBySecureId(dto.getReportedId(), appUserRepository, "User not found");
                report.setReportedUser(user);
                message = "User reported successfully";
                user.getAppUserAttribute().setReportStatus("REVIEW");
                appUserRepository.save(user);
                break;
            default:
                throw new BadRequestException("Invalid type");
        }
        reportRepository.save(report);

        return message;
    }

    @Override
    public String SendReportCommentReply(ReportRequest dto) {
        AppUser userId = GlobalConverter.getUserEntity(appUserRepository);

        Report report = new Report();
        report.setType(dto.getType());
        report.setReporterUser(userId);
        report.setReason(dto.getReason());
        report.setOtherReason(dto.getOtherReason());
        report.setStatus("DRAFT");

        CommentReply commentReply = getEntityBySecureId(dto.getReportedId(), commentReplyRepository, "Comment not found");
        report.setCommentReply(commentReply);
        String message = "Comment reply reported successfully";
        commentReply.setReportStatus("REVIEW");
        commentReplyRepository.save(commentReply);
        reportRepository.save(report);
        return message;
    }

    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listReportOnDetail(
            Integer pages, Integer limit, String sortBy, String direction, String keyword, String reportId, String detailOf) {

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Report report = HandlerRepository.getIdBySecureId(
                reportId,
                reportRepository::findBySecureId,
                projection -> reportRepository.findById(projection.getId()),
                "Report not found"
        );

        Page<ReportListDetailProjection> pageResult = null;
        if (detailOf.equals("POST")) {
            pageResult = reportRepository.getListReportOnDetail(
                    report.getPost().getId(), null, null, null, set.keyword(), set.pageable()
            );
        }
        if (detailOf.equals("COMMENT")) {
            pageResult = reportRepository.getListReportOnDetail(
                    null, report.getComment().getId(), null, null, set.keyword(), set.pageable()
            );
        }
        if (detailOf.equals("COMMENT_REPLY")) {
            pageResult = reportRepository.getListReportOnDetail(
                    null, null, report.getCommentReply().getId(), null, set.keyword(), set.pageable()
            );
        }
        if (detailOf.equals("USER")) {
            pageResult = reportRepository.getListReportOnDetail(
                    null, null, null, report.getReportedUser().getId(), set.keyword(), set.pageable()
            );
        }

        assert pageResult != null;
        List<ReportListDetailResponse> data = pageResult.getContent().stream()
                .map(d -> {
                    ReportListDetailResponse dto = new ReportListDetailResponse();
                    dto.setReporterName(d.getReporterName());
                    dto.setReason(d.getReason());
                    dto.setOtherReason(d.getOtherReason());
                    dto.setCreatedAt(Formatter.formatDateTimeApps(d.getCreatedAt()));
                    return dto;
                }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, data);
    }


    @Override
    public void updateReportStatus(ChangeStatusRequest dto) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Report report = HandlerRepository.getEntityBySecureId(dto.reportedId(), reportRepository, "Report not found");

        report.setStatus(dto.status());
        report.setUpdatedAt(LocalDateTime.now());
        report.setUpdatedBy(admin);
        reportRepository.save(report);
    }
}

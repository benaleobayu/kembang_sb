package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Report;
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

    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listReportOnDetail(
            Integer pages, Integer limit, String sortBy, String direction, String keyword, String reportId) {

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Report report = HandlerRepository.getIdBySecureId(
                reportId,
                reportRepository::findBySecureId,
                projection -> reportRepository.findById(projection.getId()),
                "Report not found"
        );

        Page<ReportListDetailProjection> pageResult = reportRepository.getListReportOnDetail(
                report.getId(), set.keyword(), set.pageable()
        );

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

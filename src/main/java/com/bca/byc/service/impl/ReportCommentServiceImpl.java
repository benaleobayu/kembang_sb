package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Post;
import com.bca.byc.model.ReportCommentDetailResponse;
import com.bca.byc.model.ReportCommentIndexResponse;
import com.bca.byc.model.projection.ReportCommentIndexProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportCommentService;
import com.bca.byc.util.PaginationUtil;
import com.bca.byc.util.helper.Formatter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportCommentServiceImpl implements ReportCommentService {

    private final ReportRepository reportRepository;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ReportCommentIndexResponse> listDataReportComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate, String reportStatus, String reportType) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, startDate, endDate, reportStatus, reportType);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<ReportCommentIndexProjection> pageResult = reportRepository.getDataReportCommentIndex(null, set.keyword(), set.pageable(), start, end, reportStatus, reportType);
        List<ReportCommentIndexResponse> dtos = new ArrayList<>(pageResult.stream()
                .map(data -> new ReportCommentIndexResponse(
                        data.getId(),
                        data.getIndex(),
                        GlobalConverter.getParseImage(data.getThumbnail(), baseUrl),
                        data.getComment(),
                        data.getCommentOwner(),
                        data.getStatusReport(),
                        data.getTotalReports(),
                        Formatter.formatLocalDateTime(data.getLastReport())
                ))
                .collect(Collectors.toMap(
                        ReportCommentIndexResponse::comment,
                        dto -> dto,
                        (existing, replacement) -> existing
                ))
                .values());

        dtos.forEach(dto -> System.out.println(dto.id()));


        int currentPage = pageResult.getNumber() + 1; // Halaman saat ini (1-based)
        long totalItems = pageResult.getTotalElements(); // Total items dari query
        int totalPages = (int) Math.ceil((double) totalItems / pageResult.getSize()); // Total halaman

        return PaginationUtil.createResultPageDTO(
                totalItems, // total items
                dtos, // data yang ditampilkan
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages ? currentPage + 1 : totalPages, // next page
                1, // first page
                totalPages, // last page
                pageResult.getSize() // per page
        );

    }


    @Override
    public ReportCommentDetailResponse findDataById(String id) {
        // set date
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Page<ReportCommentIndexProjection> pageResult = reportRepository.getDataReportCommentIndex(id, null, null, start, end, null, null);
        if (pageResult.isEmpty()) {
            throw new EntityNotFoundException("Report Comment not found for ID: " + id);
        }

        ReportCommentIndexProjection data = pageResult.getContent().get(0);
        Post post = data.getReport().getComment() != null ? data.getReport().getComment().getPost() : data.getReport().getCommentReply().getParentComment().getPost();
        List<Map<String, String>> postComment = post.getPostContents().stream()
                .map(pc -> Collections.singletonMap("Content", GlobalConverter.getParseImage(pc.getContent(), baseUrl)))
                .collect(Collectors.toList());

        return new ReportCommentDetailResponse(
                data.getReport().getSecureId(),
                data.getReport().getComment() != null ? data.getReport().getComment().getComment() : data.getReport().getCommentReply().getComment(),
                postComment
        );
    }

}

package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.projection.ReportDataProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportContentService;
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
public class ReportContentServiceImpl implements ReportContentService {

    private final AppAdminRepository adminRepository;

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate, String reportStatus, String reportType) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, startDate, endDate, reportStatus, reportType);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<ReportDataProjection> pageResult = reportRepository.getDataReportIndex(null, set.keyword(), set.pageable(), start, end, reportStatus, reportType);

        Map<String, ReportContentIndexResponse> responseMap = new HashMap<>();

        pageResult.forEach(c -> {
            String postId = c.getPost().getSecureId();
            if (!responseMap.containsKey(postId)) {
                responseMap.put(postId, new ReportContentIndexResponse(
                        c.getReport().getSecureId(),
                        c.getReport().getId(),
                        GlobalConverter.convertListToArray(c.getPost().getHighlight()),
                        GlobalConverter.getParseImage(
                                Objects.equals(c.getPostContent().getType(), "video") ?
                                        c.getPostContent().getThumbnail() :
                                        c.getPostContent().getContent(),
                                baseUrl),
                        c.getPost().getDescription(),
                        c.getTag() != null ? c.getTag().getName() : null,
                        c.getUser() != null ? c.getUser().getName() : null,
                        c.getReport().getStatus(),
                        reportRepository.countByPostId(c.getReport().getPost().getId()),
                        Formatter.formatLocalDateTime(c.getReport().getCreatedAt())
                ));
            }
        });

        List<ReportContentIndexResponse> dtos = new ArrayList<>(responseMap.values());

        return PageCreateReturn.create(pageResult, dtos);
    }


    @Override
    public ReportContentDetailResponse findDataById(String id) {
        // set date
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Page<ReportDataProjection> pageResult = reportRepository.getDataReportIndex(id, null, null, start, end, null, null);
        if (pageResult.isEmpty()) {
            throw new EntityNotFoundException("Report content not found for ID: " + id);
        }

        ReportDataProjection data = pageResult.getContent().get(0);

        List<Map<String, String>> postContent = data.getPost().getPostContents().stream()
                .map(pc -> {
                    Map<String, String> contentMap = new HashMap<>();
                    contentMap.put("content", GlobalConverter.getParseImage(pc.getContent(), baseUrl));
                    return contentMap;
                })
                .collect(Collectors.toList());

        return new ReportContentDetailResponse(
                data.getReport().getSecureId(),
                data.getUserDetail().getName(),
                Formatter.formatLocalDateTime(data.getReport().getCreatedAt()),
                data.getChannel() != null ? data.getChannel().getName() : null,
                data.getPost().getIsActive(),
                data.getPost().getDescription(),
                GlobalConverter.convertListToArray(data.getPost().getHighlight()),
                GlobalConverter.convertListToArray(data.getTag().getName()),
                postContent
        );
    }


}

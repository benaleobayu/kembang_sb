package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Tag;
import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.projection.ReportContentIndexProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.PostHasTagRepository;
import com.bca.byc.repository.ReportRepository;
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

    private final ReportRepository reportRepository;
    private final PostHasTagRepository postHasTagRepository;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate, String reportStatus) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, startDate, endDate, reportStatus);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<ReportContentIndexProjection> pageResult = reportRepository.getDataReportIndex(null, set.keyword(), set.pageable(), start, end, reportStatus);


        List<ReportContentIndexResponse> dtos = new ArrayList<>(pageResult.getContent().stream()
                .map(data -> {
                    Set<Tag> allTags = postHasTagRepository.getTagsByPostId(data.getPost().getId());
                    Set<String> tagNames = allTags.stream().map(Tag::getName).collect(Collectors.toSet());

                    return new ReportContentIndexResponse(
                            data.getId(),
                            data.getIndex(),
                            data.getHighlight() != null ? Arrays.stream(data.getHighlight().split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList()) : null,
                            GlobalConverter.getParseImage(data.getThumbnail(), baseUrl),
                            data.getDescription(),
                            tagNames.isEmpty() ? null : tagNames,
                            data.getCreator(),
                            data.getReporterEmail(),
                            data.getStatusReport(),
                            data.getTotalReport(),
                            data.getLastReportAt() != null ? Formatter.formatLocalDateTime(data.getLastReportAt()) : null
                    );
                }).collect(Collectors.toMap(
                        ReportContentIndexResponse::description,
                        dto -> dto,
                        (existing, replacement) -> existing
                ))
                .values());

        return PageCreateReturn.create(pageResult, dtos);
    }


    @Override
    public ReportContentDetailResponse findDataById(String id) {
        // set date
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Page<ReportContentIndexProjection> pageResult = reportRepository.getDataReportIndex(id, null, null, start, end, null);
        if (pageResult.isEmpty()) {
            throw new EntityNotFoundException("Report content not found for ID: " + id);
        }

        ReportContentIndexProjection data = pageResult.getContent().get(0);

        List<Map<String, String>> postContent = data.getPost().getPostContents().stream()
                .map(pc -> {
                    Map<String, String> contentMap = new HashMap<>();
                    contentMap.put("content", GlobalConverter.getParseImage(pc.getContent(), baseUrl));
                    return contentMap;
                })
                .collect(Collectors.toList());

        return new ReportContentDetailResponse(
                data.getId(),
                data.getCreator(),
                Formatter.formatLocalDateTime(data.getLastReportAt()),
                data.getChannelName() != null ? data.getChannelName() : null,
                data.getPost().getIsActive(),
                data.getPost().getDescription(),
                GlobalConverter.convertListToArray(data.getPost().getHighlight()),
                data.getTags() != null ? data.getTags().stream().map(Tag::getName).collect(Collectors.toSet()) : null,
                postContent
        );
    }


}
